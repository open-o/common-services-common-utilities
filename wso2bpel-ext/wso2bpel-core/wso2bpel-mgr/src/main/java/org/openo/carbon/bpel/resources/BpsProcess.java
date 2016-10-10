/**
 * Copyright 2016 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.carbon.bpel.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.openo.carbon.bpel.config.ConfigManager;
import org.openo.carbon.bpel.util.JsonUtil;
import org.openo.carbon.bpel.util.SoapUtil;
import org.openo.carbon.bpel.util.Xml2JsonUtil;

import com.codahale.metrics.annotation.Timed;
import com.eviware.soapui.model.iface.MessagePart;
import com.eviware.soapui.model.iface.Request;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/openoapi/wso2bpel/v1")
@Api(tags = {"wso2 bpel api"})
public class BpsProcess {

  private static final Log log = LogFactory.getLog(BpsProcess.class);

  public static final int STATUS_SUCCESS = 1;
  public static final int STATUS_FAIL = 0;

  private Map<String, String> configMap = null;

  @SuppressWarnings("unused")
  private final AtomicLong counter;

  public BpsProcess() {
    this.counter = new AtomicLong();
  }

  private synchronized String getConfig(String key) {
    if (configMap == null) {
      configMap = new HashMap<String, String>();
      String uploadFilePath = ConfigManager.getInstance().getProperty("wso2.uploadfile.path");
      String jksFile = ConfigManager.getInstance().getProperty("wso2.ssl.jks.file");
      String trustStorePassword =
          ConfigManager.getInstance().getProperty("wso2.ssl.trustStorePassword");
      String httpUsername =
          ConfigManager.getInstance().getProperty("wso2.http.authenticator.username");
      String httpPassword =
          ConfigManager.getInstance().getProperty("wso2.http.authenticator.password");
      String host = ConfigManager.getInstance().getProperty("wso2.host");
      String port = ConfigManager.getInstance().getProperty("wso2.http.port");
      configMap.put("uploadFilePath", uploadFilePath);
      configMap.put("jksFile", jksFile);
      configMap.put("trustStorePassword", trustStorePassword);
      configMap.put("httpUsername", httpUsername);
      configMap.put("httpPassword", httpPassword);
      configMap.put("host", host);
      configMap.put("port", port);
    }
    if (configMap.containsKey(key)) {
      return configMap.get(key);
    } else {
      return ConfigManager.getInstance().getProperty(key);
    }
  }

  @SuppressWarnings("unchecked")
  @POST
  @Path("process/instance")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(value = MediaType.APPLICATION_JSON)
  @ApiOperation(value = "startProcess", response = Map.class)
  @Timed
  public Map<String, Object> startProcess(JsonNode jsonObj, @Context HttpServletRequest request) {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    String errorMessage = "unkown";

    Map<String, Object> paramMap = new HashMap<String, Object>();
    try {
      paramMap = JsonUtil.json2Bean(jsonObj.toString(), Map.class);
      String processId = (String) paramMap.get("processId");

      Object params = paramMap.get("params");

      String wsdlUrl = getWsdlUrl(processId);
      String response = invokeWsdl(wsdlUrl, params);

      map.put("status", STATUS_SUCCESS);
      map.put("message", "success");
      // map.put("wsdl", wsdlUrl);
      map.put("response", response);
      return map;
    } catch (JsonParseException e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (JsonMappingException e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (IOException e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (Exception e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }

    map.put("status", STATUS_FAIL);
    map.put("message", errorMessage);
    return map;
  }

  public static String invokeWsdl(String wsdlUrl, Object params) throws Exception {
    return invokeWsdl(wsdlUrl, params, null);
  }

  @SuppressWarnings({"unused", "rawtypes"})
  public static String invokeWsdl(String wsdlUrl, Object params,
      HttpTransportProperties.Authenticator authenticator) throws Exception {
    SoapUtil soapUtil = new SoapUtil();
    Request[] requestTemplates = soapUtil.getRequestTemplate(wsdlUrl);
    String requestTemplate = null;
    Request invokeRequest = null;
    for (Request requestXML : requestTemplates) {

      InputStream is = new ByteArrayInputStream(requestXML.getRequestContent().getBytes());
      OMXMLParserWrapper builder = OMXMLBuilderFactory.createOMBuilder(
          OMAbstractFactory.getOMFactory(), StAXParserConfiguration.STANDALONE, is);
      OMElement root = builder.getDocumentElement();
      OMDocument omDocument = builder.getDocument();
      Iterator iter = omDocument.getChildren();
      OMElement bodyElement = null;
      while (iter.hasNext()) {
        OMElement node = (OMElement) iter.next();
        String nodeName = node.getLocalName();
        if (nodeName.equals("Envelope")) {
          Iterator envChildren = node.getChildElements();
          while (envChildren.hasNext()) {
            Object childNode = envChildren.next();
            if (childNode instanceof OMElement) {
              if (((OMElement) childNode).getLocalName().equals("Body")) {
                bodyElement = (OMElement) childNode;
              }
            }
          }
        }
      }
      Set<String> paramSet = new HashSet<String>();
      Iterator bodyIter = bodyElement.getChildElements();
      while (bodyIter.hasNext()) {
        Object obj = bodyIter.next();
        OMElement requestBody = (OMElement) obj;
        paramSet.add(requestBody.getLocalName());
      }
      if (params instanceof Map) {
        Set paramKeySet = ((Map) params).keySet();
        boolean matched = true;
        for (Object key : paramKeySet) {
          if (!paramSet.contains(key)) {
            matched = false;
            continue;
          }
        }
        if (matched) {
          invokeRequest = requestXML;
          requestTemplate = requestXML.getRequestContent();
          break;
        }
      }
    }
    if (requestTemplate == null) {
      throw new Exception("Invalid param.");
    }

    InputStream is = new ByteArrayInputStream(requestTemplate.getBytes());
    OMXMLParserWrapper builder = OMXMLBuilderFactory
        .createOMBuilder(OMAbstractFactory.getOMFactory(), StAXParserConfiguration.STANDALONE, is);
    OMElement root = builder.getDocumentElement();
    OMDocument omDocument = builder.getDocument();
    Iterator iter = omDocument.getChildren();
    OMElement bodyElement = null;
    while (iter.hasNext()) {
      OMElement node = (OMElement) iter.next();
      String nodeName = node.getLocalName();
      if (nodeName.equals("Envelope")) {
        Iterator envChildren = node.getChildElements();
        while (envChildren.hasNext()) {
          Object childNode = envChildren.next();
          if (childNode instanceof OMElement) {
            if (((OMElement) childNode).getLocalName().equals("Body")) {
              bodyElement = (OMElement) childNode;
            }
          }
        }
      }
    }

    Options options = new Options();
    EndpointReference targetEPR = new EndpointReference(wsdlUrl);
    options.setTo(targetEPR);
    if (authenticator != null) {
      options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);
    }
    ServiceClient sender = new ServiceClient();
    sender.setOptions(options);
    OMFactory factory = OMAbstractFactory.getOMFactory();
    OMElement requestBody = null;
    Iterator bodyIter = bodyElement.getChildElements();
    while (bodyIter.hasNext()) {
      Object obj = bodyIter.next();
      requestBody = (OMElement) obj;
    }

    // Iterator requestBodyIter = requestBody.getChildElements();
    setParams(requestBody, params);

    requestBody.build();
    OMElement result = null;
    boolean needResponse = false;
    try {
      MessagePart[] parts = invokeRequest.getResponseParts();
      if (parts != null && parts.length > 0) {
        needResponse = true;
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    if (needResponse) {
      result = sender.sendReceive(requestBody);
      return Xml2JsonUtil.xml2JSON(result.toString());
    } else {
      sender.sendRobust(requestBody);
      return "";
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void setParams(OMElement requestBody, Object params) {
    Object currentParams = null;
    if (params instanceof Map) {
      currentParams = ((Map) params).get((requestBody).getLocalName());
      Object obj = requestBody.getFirstElement();
      if (obj == null) {
        if (currentParams instanceof String) {
          requestBody.setText((String) currentParams);
        }
      } else {
        Iterator<OMElement> iter = requestBody.getChildElements();
        while (iter.hasNext()) {
          OMElement child = iter.next();
          setParams((OMElement) child, currentParams);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public String getWsdlUrl(String pid) {
    log.warn("rest begin...");
    String wsdlUrl = null;
    try {
      System.setProperty("javax.net.ssl.trustStore", "*.keystore");

      System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");

      System.setProperty("javax.net.ssl.trustStore", getConfig("jksFile"));
      System.setProperty("javax.net.ssl.trustStorePassword", getConfig("trustStorePassword"));

      String url = "https://" + getConfig("host") + ":" + getConfig("port")
          + "/services/ProcessManagementService?wsdl";
      RPCServiceClient serviceClient = new RPCServiceClient();
      EndpointReference targetEPR = new EndpointReference(url);
      Options options = serviceClient.getOptions();
      options.setTo(targetEPR);
      // options.setAction("sch:undeployBPELPackage");

      options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(300000));
      HttpTransportProperties.Authenticator authenticator =
          new HttpTransportProperties.Authenticator();
      List<String> auth = new ArrayList<String>();
      auth.add(Authenticator.BASIC);
      authenticator.setAuthSchemes(auth);
      authenticator.setUsername(getConfig("httpUsername"));
      authenticator.setPassword(getConfig("httpPassword"));
      authenticator.setPreemptiveAuthentication(true);
      options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);

      serviceClient.setOptions(options);

      OMFactory fac = OMAbstractFactory.getOMFactory();
      OMNamespace omNs = fac.createOMNamespace("http://wso2.org/bps/management/schema", "sch");

      OMElement method = fac.createOMElement("getProcessInfoIn", omNs);
      QName pidQName = QName.valueOf(pid);
      OMElement pidElement = fac.createOMElement("pid", omNs);

      pidElement.addChild(fac.createOMText(pidElement, pidQName));
      method.addChild(pidElement);

      method.build();

      OMElement res = serviceClient.sendReceive(method);
      res.getFirstElement().getText();

      // System.out.println(JsonUtil.bean2Json(parse(res)));

      Iterator<OMElement> iter = res.getChildrenWithLocalName("endpoints");
      if (iter.hasNext()) {
        OMElement endPoints = (OMElement) iter.next();
        Iterator<OMElement> endPointIter = endPoints.getChildrenWithLocalName("endpointRef");
        if (endPointIter.hasNext()) {
          OMElement endpointRef = (OMElement) endPointIter.next();
          Iterator<OMElement> endpointRefIter =
              endpointRef.getChildrenWithLocalName("serviceLocations");
          if (endpointRefIter.hasNext()) {
            OMElement serviceLocations = (OMElement) endpointRefIter.next();
            Iterator<OMElement> serviceLocationsIter =
                serviceLocations.getChildrenWithLocalName("serviceLocation");
            while (serviceLocationsIter.hasNext()) {
              OMElement serviceLocation = (OMElement) serviceLocationsIter.next();
              String sUrl = serviceLocation.getText();
              if (sUrl.endsWith("wsdl")) {
                wsdlUrl = sUrl;
                break;
              }
            }
          }
        }
      }

    } catch (AxisFault e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (Throwable e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } finally {
      log.warn("invoke finally...");
    }
    return wsdlUrl;
  }

  @SuppressWarnings({"unused", "rawtypes", "unchecked"})
  private Map parse(OMElement node) {
    Map resultMap = new HashMap();
    Iterator attrIter = node.getAllAttributes();
    while (attrIter.hasNext()) {
      OMAttribute attr = (OMAttribute) attrIter.next();
      resultMap.put(attr.getLocalName(), attr.getAttributeValue());
    }
    boolean hasChild = false;
    List<Map> childList = new ArrayList<Map>();
    Iterator childIter = node.getChildren();// getChildElements();
    while (childIter.hasNext()) {
      hasChild = true;
      Object child = childIter.next();
      if (child instanceof OMText) {
        resultMap.put(node.getLocalName(), ((OMText) child).getText());
      } else if (child instanceof OMElement) {
        childList.add(parse((OMElement) child));
        resultMap.put(node.getLocalName(), childList);
      }
    }
    return resultMap;
  }

  public static void main1(String[] args) {
    Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
    WebTarget target = client.target("http://127.0.0.1:8080/wso2bpel/v1/process/instance");
    String jsonObj =
        "{\"processId\":\"{http://ode/bpel/unit-test}HelloWorld2-18\",\"params\": {\"hello\":{\"TestPart\":\"AAA\"}}}";
    Response response = target.request(MediaType.APPLICATION_JSON)
        .post(Entity.entity(jsonObj, MediaType.APPLICATION_JSON));
    int responseCode = response.getStatus();
    if (responseCode == 200) {
      String excuteRespJson = response.readEntity(String.class);
      // context.setExcuteRespJson(excuteRespJson);
      // System.out.println("excute responseJson=====" +
      // context.getExcuteRespJson());
      System.out.println(excuteRespJson);
    }

    System.out.println("************************************************************************");
    jsonObj =
        "{\"processId\":\"{http://ode/bpel/unit-test}HelloWorld2-18\",\"params\": {\"hello\":{\"TestPart\":\"AAA\"}}}";
    BpsProcess process = new BpsProcess();
    try {
      Map<String, Object> resultMap = process.startProcess(JsonUtil.getJsonNode(jsonObj), null);
      System.out.println(JsonUtil.bean2Json(resultMap));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("************************************************************************");
    jsonObj =
        "{\"processId\":\"{http://ode/bpel/unit-test}HelloXslWorld-3\",\"params\": {\"helloXsl\":{\"TestPart\":{\"content\":\"AAA\"}}}}";
    process = new BpsProcess();
    try {
      Map<String, Object> resultMap = process.startProcess(JsonUtil.getJsonNode(jsonObj), null);
      System.out.println(JsonUtil.bean2Json(resultMap));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("************************************************************************");
    jsonObj =
        "{\"processId\":\"{http://wso2.org/bps/samples/While}While-5\",\"params\": {\"WhileRequest\":{\"input\":\"365\"}}}";
    process = new BpsProcess();
    try {
      Map<String, Object> resultMap = process.startProcess(JsonUtil.getJsonNode(jsonObj), null);
      System.out.println(JsonUtil.bean2Json(resultMap));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
    }

  }

  @SuppressWarnings("unchecked")
  public static void main(String[] args) {

    Map<String, Object> map = new LinkedHashMap<String, Object>();
    String errorMessage = "unkown";

    Map<String, Object> paramMap = new HashMap<String, Object>();
    try {
      String param =
          "{\"planInput\":{\"sfc_count\":\"2\",\"nsInstanceId\":\"223\",\"vnfmId\":\"112\",\"instanceId\":\"334\",\"object_context\":\"{\\\"e\\\":{\\\"f\\\":\\\"4\\\"}}\",\"vnf_count\":\"2\",\"serviceTemplateId\":\"?\",\"vl_count\":\"2\",\"containerapiUrl\":\"?\",\"object_additionalParamForVnf\":\"[{\\\"b\\\":1},{\\\"c\\\":{\\\"d\\\":\\\"2\\\"}}}]\",\"object_additionalParamForNs\":\"[{\\\"a\\\":3},{\\\"e\\\":{\\\"f\\\":\\\"4\\\"}}}]\"}}";
      String jsonObj =
          "{\"processId\":\"{http://wso2.org/bps/samples/While}While-5\",\"params\":" + param + "}";
      paramMap = JsonUtil.json2Bean(jsonObj.toString(), Map.class);
      Object params = paramMap.get("params");
      String wsdlUrl = "http://10.74.151.36:9763/services/initService?wsdl";
      String response = invokeWsdl(wsdlUrl, params);
      map.put("status", STATUS_SUCCESS);
      map.put("message", "success");
      map.put("response", response);
      System.out.println(JsonUtil.bean2Json(map));
    } catch (JsonParseException e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (JsonMappingException e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (IOException e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (Exception e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }

    map.put("status", STATUS_FAIL);
    map.put("message", errorMessage);
    try {
      System.out.println(JsonUtil.bean2Json(map));
    } catch (IOException e) {
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }

  }
}

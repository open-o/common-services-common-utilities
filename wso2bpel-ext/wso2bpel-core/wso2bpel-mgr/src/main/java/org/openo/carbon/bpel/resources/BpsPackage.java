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

import java.io.File;
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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.HttpTransportProperties.Authenticator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.wso2.carbon.bpel.stub.upload.types.UploadedFileItem;
import org.openo.carbon.bpel.common.Config;
import org.openo.carbon.bpel.util.JsonUtil;
import org.openo.carbon.bpel.util.Xml2JsonUtil;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/")
@Api(tags = {"wso2 bpel api"})
public class BpsPackage {

  private static final Log log = LogFactory.getLog(BpsPackage.class);

  public static final int STATUS_SUCCESS = 1;
  public static final int STATUS_FAIL = 0;
  /**
   * 无错误
   */
  public static final int ERROR_CODE_NOERROR = 0;
  /**
   * 不支持的文件类型
   */
  public static final int ERROR_CODE_PACKAGE_UNSUPPORED_FILE = 10001;
  /**
   * 同名的包正在操作中
   */
  public static final int ERROR_CODE_PACKAGE_STATUS_BUSY = 10002;
  /**
   * 包名重复
   */
  public static final int ERROR_CODE_PACKAGE_DUPLICATED_NAME = 10003;
  /**
   * 未获取到包部署的信息
   */
  public static final int ERROR_CODE_PACKAGE_DEPLOY_INFORMATION_IS_LOST = 10004;
  /**
   * 卸载包失败
   */
  public static final int ERROR_CODE_PACKAGE_UNDEPLOY_FAILED = 10005;
  /**
   * 包不存在
   */
  public static final int ERROR_CODE_PACKAGE_NOTEXISTS = 10006;
  /**
   * 服务运行时异常
   */
  public static final int ERROR_CODE_RUNTIME_EXCEPTION = 20001;
  /**
   * Axis运行时异常
   */
  public static final int ERROR_CODE_RUNTIME_EXCEPTION_AXIS = 20002;
  /**
   * IO运行时异常
   */
  public static final int ERROR_CODE_RUNTIME_EXCEPTION_IO = 20003;

  public static Set<String> packageNameSet = new HashSet<String>();

  private Map<String, String> configMap = null;

  @SuppressWarnings("unused")
  private final AtomicLong counter;

  public BpsPackage() {
    this.counter = new AtomicLong();
  }

  private synchronized String getConfig(String key) {
    if (configMap == null) {
      configMap = new HashMap<String, String>();
      String uploadFilePath = Config.getConfigration().getWso2UploadFilePath();
      String jksFile = Config.getConfigration().getWso2SslJksFile();
      String trustStorePassword = Config.getConfigration().getWso2SslJksPassword();
      String httpUsername = Config.getConfigration().getWso2AuthUserName();
      String httpPassword = Config.getConfigration().getWso2AuthPassword();
      String host = Config.getConfigration().getWso2Host();
      String port = Config.getConfigration().getWso2HostPort();
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
      return "";
    }
  }

  public static boolean lockPackageName(String packageName) {
    boolean succeed = false;
    synchronized (packageNameSet) {
      if (!packageNameSet.contains(packageName)) {
        packageNameSet.add(packageName);
        succeed = true;
      }
    }
    return succeed;
  }

  public static boolean unlockPackageName(String packageName) {
    boolean succeed = false;
    synchronized (packageNameSet) {
      if (packageNameSet.contains(packageName)) {
        packageNameSet.remove(packageName);
        succeed = true;
      }
    }
    return succeed;
  }

  @POST
  @Path(value = "package")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(value = MediaType.APPLICATION_JSON)
  @ApiOperation(value = "package process", response = Map.class)
  @Timed
  public Map<String, Object> uploadFile(@FormDataParam("filename") String filename,
      @FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail,
      @Context HttpServletRequest request, @Context HttpServletResponse response)
          throws IOException {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    String errorMessage = "unkown";
    int errorCode = ERROR_CODE_NOERROR;

    String fileName = fileDetail.getFileName();
    String fullName = getConfig("uploadFilePath") + "/" + fileName;
    File file = new File(fullName);
    String packageName = null;
    try {
      if (fileName.endsWith(".zip")) {
        packageName = fileName.substring(0, fileName.length() - 4);
      } else {
    	  errorCode = ERROR_CODE_PACKAGE_UNSUPPORED_FILE;
        throw new Exception("Only support *.zip file.");
      }
      if (!lockPackageName(packageName)) {
    	  errorCode = ERROR_CODE_PACKAGE_STATUS_BUSY;
        throw new Exception("Package " + packageName + " is operating.");
      }
      FileUtils.copyInputStreamToFile(fileInputStream, file);
      System.setProperty("javax.net.ssl.trustStore", "*.keystore");
      System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
      System.setProperty("javax.net.ssl.trustStore", getConfig("jksFile"));
      System.setProperty("javax.net.ssl.trustStorePassword", getConfig("trustStorePassword"));

      HttpTransportProperties.Authenticator authenticator =
          new HttpTransportProperties.Authenticator();
      List<String> auth = new ArrayList<String>();
      auth.add(Authenticator.BASIC);
      authenticator.setAuthSchemes(auth);
      authenticator.setUsername(getConfig("httpUsername"));
      authenticator.setPassword(getConfig("httpPassword"));
      authenticator.setPreemptiveAuthentication(true);

      map.putAll(bpsDeployPackage(fileName, fullName, packageName, authenticator));
      map.put("status", STATUS_SUCCESS);
      map.put("message", "success");
      return map;
    } catch (AxisFault e) {
      errorMessage = e.getLocalizedMessage();
      errorCode = ERROR_CODE_RUNTIME_EXCEPTION_AXIS;
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (IOException e) {
      errorMessage = e.getLocalizedMessage();
      errorCode = ERROR_CODE_RUNTIME_EXCEPTION_IO;
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (Throwable e) {
      errorMessage = e.getLocalizedMessage();
		if (e instanceof BpsServiceException) {
			errorCode = ((BpsServiceException) e).getErrorCode();
		} else {
			errorCode = ERROR_CODE_RUNTIME_EXCEPTION;
		}
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } finally {
      if (packageName != null) {
        unlockPackageName(packageName);
      }
    }
    map.put("errorCode", errorCode);
    map.put("status", STATUS_FAIL);
    map.put("message", errorMessage);
    return map;
  }

  @SuppressWarnings("rawtypes")
  private Map<String, Object> bpsDeployPackage(String fileName, String fullName, String packageName,
      HttpTransportProperties.Authenticator authenticator)
          throws JsonParseException, JsonMappingException, IOException, AxisFault, Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map deployedPackageInfoMap = getDeployedPackageInfo(authenticator, packageName, "");
    if (deployedPackageInfoMap.get("packageName") != null) {
      throw new BpsServiceException(ERROR_CODE_PACKAGE_DUPLICATED_NAME,"Package " + deployedPackageInfoMap.get("packageName")
          + " exist, please undeploy it first.");
    }
    OMElement element = deployPackage(fileName, fullName, authenticator);
    System.out.println(Xml2JsonUtil.xml2JSON(element.toString()));

    long timeout = 60 * 1000L;
    long timeStart = System.currentTimeMillis();
    while (System.currentTimeMillis() - timeStart < timeout) {
      deployedPackageInfoMap = getDeployedPackageInfo(authenticator, packageName, "");
      if (deployedPackageInfoMap.get("packageName") == null) {
        Thread.sleep(2000);
      } else {
        break;
      }
    }
    if (deployedPackageInfoMap.get("packageName") == null) {
      throw new BpsServiceException(ERROR_CODE_PACKAGE_DEPLOY_INFORMATION_IS_LOST,
          "Package " + packageName + " deploy failed or deploy information is lost.");
    }
    map.put("packageName", deployedPackageInfoMap.get("packageName"));
    map.put("processId", deployedPackageInfoMap.get("pid"));
    return map;
  }

  private OMElement deployPackage(String fileName, String fullName,
      HttpTransportProperties.Authenticator authenticator) throws AxisFault {
    String url =
        "https://" + getConfig("host") + ":" + getConfig("port") + "/services/BPELUploader?wsdl";
    RPCServiceClient serviceClient = new RPCServiceClient();
    EndpointReference targetEPR = new EndpointReference(url);
    Options options = serviceClient.getOptions();
    options.setTo(targetEPR);
    options.setAction("urn:uploadService");
    options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(300000));
    options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);
    serviceClient.setOptions(options);

    QName qname = new QName("http://services.deployer.bpel.carbon.wso2.org", "uploadService");
    UploadedFileItem fileItem = new UploadedFileItem();
    fileItem.setFileName(fileName);
    fileItem.setFileType("zip");

    DataSource dataSource = new FileDataSource(fullName);
    fileItem.setDataHandler(new DataHandler(dataSource));
    UploadedFileItem[] parameters = new UploadedFileItem[] {fileItem};
    OMElement element = serviceClient.invokeBlocking(qname, parameters);
    return element;
  }

  @Path(value = "package/{packageName}")
  @DELETE
  @Produces(value = MediaType.APPLICATION_JSON)
  @ApiOperation(value = "delete", response = Map.class)
  @Timed
  public Map<String, Object> delete(@PathParam("packageName") String packageName,
      @Context HttpServletRequest request) {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    String errorMessage = "unkown";
    int errorCode = ERROR_CODE_NOERROR;
    try {
      if (!lockPackageName(packageName)) {
        throw new Exception("Package " + packageName + " is operating.");
      }
      System.setProperty("javax.net.ssl.trustStore", "*.keystore");
      System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
      System.setProperty("javax.net.ssl.trustStore", getConfig("jksFile"));
      System.setProperty("javax.net.ssl.trustStorePassword", getConfig("trustStorePassword"));

      HttpTransportProperties.Authenticator authenticator =
          new HttpTransportProperties.Authenticator();
      List<String> auth = new ArrayList<String>();
      auth.add(Authenticator.BASIC);
      authenticator.setAuthSchemes(auth);
      authenticator.setUsername(getConfig("httpUsername"));
      authenticator.setPassword(getConfig("httpPassword"));
      authenticator.setPreemptiveAuthentication(true);

      map.putAll(bpsUndeployPackage(packageName, authenticator));

      return map;
    } catch (AxisFault e) {
      errorMessage = e.getLocalizedMessage();
      errorCode = ERROR_CODE_RUNTIME_EXCEPTION_AXIS;
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (Throwable e) {
      errorMessage = e.getLocalizedMessage();
		if (e instanceof BpsServiceException) {
			errorCode = ((BpsServiceException) e).getErrorCode();
		} else {
			errorCode = ERROR_CODE_RUNTIME_EXCEPTION;
		}
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } finally {
      if (packageName != null) {
        unlockPackageName(packageName);
      }
    }
    map.put("errorCode", errorCode);
    map.put("status", STATUS_FAIL);
    map.put("message", errorMessage);
    return map;
  }

  @SuppressWarnings({"rawtypes", "unused"})
  private Map<String, Object> bpsUndeployPackage(String packageName,
      HttpTransportProperties.Authenticator authenticator)
          throws JsonParseException, JsonMappingException, IOException, AxisFault, Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map deployedPackageInfoMap = getDeployedPackageInfo(authenticator, packageName, "");
    if (deployedPackageInfoMap.get("packageName") == null) {
      throw new BpsServiceException(ERROR_CODE_PACKAGE_NOTEXISTS,"Package " + deployedPackageInfoMap.get("packageName")
          + " does not exist, please deploy it first.");
    }
    OMElement element = undeployPackage(authenticator, packageName);
    deployedPackageInfoMap = getDeployedPackageInfo(authenticator, packageName, "");
    if (deployedPackageInfoMap.get("packageName") != null) {
      throw new BpsServiceException(ERROR_CODE_PACKAGE_UNDEPLOY_FAILED,
          "Package " + deployedPackageInfoMap.get("packageName") + " undeploy failed.");
    }
    map.put("status", STATUS_SUCCESS);
    map.put("message", "success");

    return map;
  }

  private OMElement undeployPackage(HttpTransportProperties.Authenticator authenticator,
      String packageName) throws AxisFault {
    String url = "https://" + getConfig("host") + ":" + getConfig("port")
        + "/services/BPELPackageManagementService?wsdl";
    RPCServiceClient serviceClient = new RPCServiceClient();
    EndpointReference targetEPR = new EndpointReference(url);
    Options options = serviceClient.getOptions();
    options.setTo(targetEPR);
    options.setAction("sch:undeployBPELPackage");
    options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(300000));
    options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);
    serviceClient.setOptions(options);
    OMFactory fac = OMAbstractFactory.getOMFactory();
    OMNamespace omNs = fac.createOMNamespace("http://wso2.org/bps/management/schema", "sch");
    OMElement method = fac.createOMElement("undeployBPELPackage", omNs);
    OMElement content = fac.createOMElement("package", omNs);
    content.addChild(fac.createOMText(content, packageName));
    method.addChild(content);
    method.build();
    OMElement res = serviceClient.sendReceive(method);
    return res;
  }
  
  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map getFullDeployedPackageInfo(HttpTransportProperties.Authenticator authenticator,
	      String packageName, String page) throws Exception {
	    String result;
	    String jsonTemplate =
	        "{'listDeployedPackagesPaginated':{'page':'${page}','packageSearchString':'${searchString}'}}";
	    Map jsonParamMap = new HashMap();
	    jsonParamMap.put("page", page);
	    jsonParamMap.put("searchString", packageName);
	    Object params = getParams(jsonTemplate, jsonParamMap);
	    result = BpsProcess.invokeWsdl("https://" + getConfig("host") + ":" + getConfig("port")
	        + "/services/BPELPackageManagementService?wsdl", params, authenticator);
	    Map<String, Object> processMap = JsonUtil.json2Bean(result, Map.class);
	    return processMap;
	  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map getDeployedPackageInfo(HttpTransportProperties.Authenticator authenticator,
	      String packageName, String page) throws Exception {
	    Map resultMap = new HashMap();
	    Map<String, Object> processMap = getFullDeployedPackageInfo(authenticator, packageName, page);
	    Object packages = ((Map) processMap.get("deployedPackagesPaginated")).get("package");
	    Map deployedPackage = null;
	    if (packages instanceof List) {
	      for (Iterator iter = ((List) packages).iterator(); iter.hasNext();) {
	        Map packageMap = (Map) iter.next();
	        String deployedPackageName = (String) packageMap.get("name");
	        if (deployedPackageName.equals(packageName)) {
	          deployedPackage = packageMap;
	          break;
	        }
	      }
	    } else if (packages instanceof Map) {
	      String deployedPackageName = (String) ((Map) packages).get("name");
	      if (deployedPackageName.equals(packageName)) {
	        deployedPackage = (Map) packages;
	      }
	    }
	    if (deployedPackage != null) {
	      String fullPackageName = null;
	      String pid = null;
	      Object versions = ((Map) deployedPackage.get("versions")).get("version");
	      Map lastestVersion = null;
	      if (versions instanceof List) {
	        for (Iterator iter = ((List) versions).iterator(); iter.hasNext();) {
	          Map version = (Map) iter.next();
	          if (version.get("isLatest").equals("true")) {
	            lastestVersion = version;
	          }
	        }
	      } else if (versions instanceof Map) {
	        lastestVersion = (Map) versions;
	      }
	      fullPackageName = (String) ((Map) ((Map) lastestVersion.get("processes")).get("process"))
	          .get("packageName");
	      pid = (String) ((Map) ((Map) lastestVersion.get("processes")).get("process")).get("pid");
	      resultMap.put("packageName", fullPackageName);
	      resultMap.put("pid", pid);
	    }
	    return resultMap;
}


  @SuppressWarnings({"rawtypes", "unchecked"})
  public static Map<String, Object> getParams(String jsonTemplate, Map<String, Object> paramMap)
      throws JsonParseException, JsonMappingException, IOException {
    String json = jsonTemplate.replaceAll("'", "\"");
		for (Iterator iter = paramMap.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String value = "";
			if (paramMap.get(key) != null) {
				value = paramMap.get(key).toString().replaceAll("\"", "\\\\\"");
			}
			json = json.replaceAll("\\$\\{" + key + "\\}", value);
		}
    return JsonUtil.json2Bean(json, Map.class);
  }
  
  @GET
  @Path(value = "listPackages")
  @Produces(value = MediaType.APPLICATION_JSON)
  @ApiOperation(value = "list packages", response = Map.class)
  @Timed
  public Map<String, Object> listPackages(@QueryParam("page") String page,
	      @QueryParam("searchString") String searchString, @Context HttpServletRequest request)
          throws IOException {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    String errorMessage = "unkown";
    int errorCode = ERROR_CODE_NOERROR;
    String packageName = null;
    try {
      System.setProperty("javax.net.ssl.trustStore", "*.keystore");
      System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
      System.setProperty("javax.net.ssl.trustStore", getConfig("jksFile"));
      System.setProperty("javax.net.ssl.trustStorePassword", getConfig("trustStorePassword"));

      HttpTransportProperties.Authenticator authenticator =
          new HttpTransportProperties.Authenticator();
      List<String> auth = new ArrayList<String>();
      auth.add(Authenticator.BASIC);
      authenticator.setAuthSchemes(auth);
      authenticator.setUsername(getConfig("httpUsername"));
      authenticator.setPassword(getConfig("httpPassword"));
      authenticator.setPreemptiveAuthentication(true);

      map.putAll(getFullDeployedPackageInfo(authenticator,searchString,page));

      map.put("status", STATUS_SUCCESS);
      map.put("message", "success");
      return map;
    } catch (AxisFault e) {
      errorMessage = e.getLocalizedMessage();
      errorCode = ERROR_CODE_RUNTIME_EXCEPTION_AXIS;
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (IOException e) {
      errorMessage = e.getLocalizedMessage();
      errorCode = ERROR_CODE_RUNTIME_EXCEPTION_IO;
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } catch (Throwable e) {
			if (e instanceof BpsServiceException) {
				errorCode = ((BpsServiceException) e).getErrorCode();
			} else {
				errorCode = ERROR_CODE_RUNTIME_EXCEPTION;
			}
      errorMessage = e.getLocalizedMessage();
      log.error(e.getMessage(), e);
      e.printStackTrace();
    } finally {
      if (packageName != null) {
        unlockPackageName(packageName);
      }
    }
    map.put("errorCode", errorCode);
    map.put("status", STATUS_FAIL);
    map.put("message", errorMessage);
    return map;
  }

  @SuppressWarnings("rawtypes")
  public static void main(String[] args) {
    try {

      boolean remoteDebug = true;
      boolean localDebug = false;
      if (remoteDebug) {
        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        WebTarget target = client.target("http://localhost:8101/openoapi/wso2bpel/v1/package");
        FileDataBodyPart bodyPart = new FileDataBodyPart("file",
            new File("F:\\wso2bps-3.5.1\\wso2bps-3.5.1\\repository\\samples\\bpel\\Alarm.zip"));
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.field("fileName", "Alarm.zip").bodyPart(bodyPart);
        String result = target.request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(formDataMultiPart, formDataMultiPart.getMediaType()), String.class);
        System.out.println(result);
      }
      //
      // System.out.println("************************************************************************");
      //
      // bodyPart = new FileDataBodyPart("file", new
      // File("D:\\temp\\bpel-sample\\AssignDate\\AssignDate.zip"));
      // formDataMultiPart = new FormDataMultiPart();
      // formDataMultiPart.field("fileName",
      // "AssignDate.zip").bodyPart(bodyPart);
      // result = target.request(MediaType.APPLICATION_JSON)
      // .post(Entity.entity(formDataMultiPart,
      // formDataMultiPart.getMediaType()), String.class);
      // System.out.println(result);
      //
      if (localDebug) {
        System.setProperty("javax.net.ssl.trustStore", "*.keystore");
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        System.setProperty("javax.net.ssl.trustStore",
            "D:\\software\\WSO2\\wso2bps-3.5.1\\repository\\resources\\security\\wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        HttpTransportProperties.Authenticator authenticator =
            new HttpTransportProperties.Authenticator();
        List<String> auth = new ArrayList<String>();
        auth.add(Authenticator.BASIC);
        authenticator.setAuthSchemes(auth);
        authenticator.setUsername("admin");
        authenticator.setPassword("admin");
        authenticator.setPreemptiveAuthentication(true);
        //
        // String packageName = "RESTProcess";
        // Map deployedPackageInfoMap =
        // getDeployedPackageInfo(authenticator, packageName);

        BpsPackage packageTest = new BpsPackage();
        Map map;
        // map = packageTest.bpsUndeployPackage("AssignDate",
        // authenticator);
        // System.out.println(JsonUtil.bean2Json(map));
        map = packageTest.bpsDeployPackage("AssignDate.zip",
            "D:\\temp\\bpel-sample\\AssignDate\\AssignDate.zip", "AssignDate", authenticator);
        System.out.println(JsonUtil.bean2Json(map));
        map = packageTest.bpsUndeployPackage("AssignDate", authenticator);
        System.out.println(JsonUtil.bean2Json(map));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

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
package org.openo.carbon.bpel.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Request;

public class SoapUtil {
  private static final Logger log = Logger.getLogger(SoapUtil.class);

  private DocumentBuilderFactory docBuilderFactory;
  private Map<String, WsdlInterface[]> wsdls = new LRULinkedHashMap<String, WsdlInterface[]>(256);

  public SoapUtil() {
    docBuilderFactory = DocumentBuilderFactory.newInstance();
    docBuilderFactory.setNamespaceAware(true);
  }

  public Request[] getRequestTemplate(String wsdlUrl)
      throws UnsupportedOperationException, IOException {
    Request[] requests = new Request[0];
    List<Request> requestList = new ArrayList<Request>();
    Operation operationInst = null;
    WsdlInterface[] wsdlInterfaces = getWsdlInterfaces(wsdlUrl, null);
    for (WsdlInterface wsdlInterface : wsdlInterfaces) {
      Operation opr = wsdlInterface.getOperationAt(0);
      if (opr != null) {
        operationInst = opr;
        String requestTemplate = operationInst.getRequestAt(0).getRequestContent();
        requestList.add(operationInst.getRequestAt(0));
      }
    }
    requests = requestList.toArray(new Request[0]);
    return requests;
  }

  /**
   * 
   * @param wsdl
   * @param operation
   * @param httpClientProps
   * @return
   * @throws IOException
   * @throws UnsupportedOperationException
   */
  private Operation getOperation(String wsdl, String operation, Properties httpClientProps)
      throws IOException, UnsupportedOperationException {
    WsdlInterface[] wsdlInterfaces = getWsdlInterfaces(wsdl, httpClientProps);
    for (WsdlInterface wsdlInterface : wsdlInterfaces) {
      Operation operationInst = wsdlInterface.getOperationByName(operation);

      if (operationInst != null) {
        return operationInst;
      }
    }
    wsdls.remove(wsdl);
    wsdlInterfaces = getWsdlInterfaces(wsdl, httpClientProps);
    for (WsdlInterface wsdlInterface : wsdlInterfaces) {
      Operation operationInst = wsdlInterface.getOperationByName(operation);
      if (operationInst != null) {
        return operationInst;
      }
    }

    throw new UnsupportedOperationException(
        "Operation '" + operation + "' not supported by WSDL '" + wsdl + "'.");
  }

  /**
   * 
   * @param wsdl
   * @param httpClientProps
   * @return
   * @throws IOException
   */
  private WsdlInterface[] getWsdlInterfaces(String wsdl, Properties httpClientProps)
      throws IOException {
    try {
      WsdlInterface[] wsdlInterfaces = wsdls.get(wsdl);
      if (wsdlInterfaces == null) {
        WsdlProject wsdlProject = new WsdlProject();
        WsdlLoader wsdlLoader = createWsdlLoader(wsdl, httpClientProps);

        wsdlInterfaces = wsdlProject.importWsdl(wsdl, true, wsdlLoader);

        wsdls.put(wsdl, wsdlInterfaces);
      }
      return wsdlInterfaces;
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw new RuntimeException("Failed to import WSDL '" + wsdl + "'.");
    }
  }

  /**
   * 
   * @param wsdl
   * @param httpClientProps
   * @return
   * @throws ConfigurationException
   */
  private WsdlLoader createWsdlLoader(String wsdl, Properties httpClientProps)
      throws ConfigurationException {
    HttpClient httpClient = new HttpClient();
    return new ClientWsdlLoader(wsdl, httpClient);
  }

  public static void main(String[] args) {
    SoapUtil soapUtil = new SoapUtil();

    try {
      String url = "http://10.74.151.36:9763/services/initService?wsdl";
      Request[] requestXMLs = soapUtil.getRequestTemplate(url);

      for (Request requestXML : requestXMLs) {
        String requestJson = Xml2JsonUtil.xml2JSON(requestXML.getRequestContent());
        System.out.println("====================================");
        System.out.println(requestJson);
      }

    } catch (UnsupportedOperationException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}

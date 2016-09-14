/**
 * Copyright 2016 [ZTE] and others.
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Xml2JsonUtil {
  /**
   * transform xml to json
   * 
   * @param xml xml format string
   * @return return json string when success; otherwise return null
   */
  public static String xml2JSON(String xml) {
    JSONObject obj = new JSONObject();
    try {
      InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
      SAXBuilder sb = new SAXBuilder();
      Document doc = sb.build(is);
      Element root = doc.getRootElement();
      obj.put(root.getName(), iterateElement(root));
      return obj.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * transform xml file to json string
   * 
   * @param file java.io.File is an effective xml file
   * @return return json string when success; otherwise return null
   */
  public static String xml2JSON(File file) {
    JSONObject obj = new JSONObject();
    try {
      SAXBuilder sb = new SAXBuilder();
      Document doc = sb.build(file);
      Element root = doc.getRootElement();
      obj.put(root.getName(), iterateElement(root));
      return obj.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * an iteration function
   * 
   * @param parentElement : org.jdom.Element
   * @return java.util.Map
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private static Map iterateElement(Element parentElement) {
    List node = parentElement.getChildren();
    Element element = null;
    Map map = new HashMap();
    List list = null;
    for (int i = 0; i < node.size(); i++) {
      element = (Element) node.get(i);
      if (element.getTextTrim().equals("")) {
        if (element.getChildren().size() == 0)
          continue;
        if (map.containsKey(element.getName())) {
          Object obj = map.get(element.getName());
          if (obj instanceof Map) {
            list = new LinkedList();
            list.add(obj);
            list.add(iterateElement(element));
            map.remove(element.getName());
            map.put(element.getName(), list);
          } else if (obj instanceof List) {
            list = (List) obj;
            list.add(iterateElement(element));
          }
        } else {
          map.put(element.getName(), iterateElement(element));
        }
      } else {
        map.put(element.getName(), element.getTextTrim());
      }
    }
    return map;
  }

  public static void main(String[] args) {
    System.out.println(Xml2JsonUtil.xml2JSON("<MapSet>" + "<MapGroup id='Sheboygan'>" + "<Map>"
        + "<Type>MapGuideddddddd</Type>" + "<SingleTile>true</SingleTile>" + "<Extension>"
        + "<ResourceId>ddd</ResourceId>" + "</Extension>" + "</Map>" + "<Map>" + "<Type>ccc</Type>"
        + "<SingleTile>ggg</SingleTile>" + "<Extension>" + "<ResourceId>aaa</ResourceId>"
        + "</Extension>" + "</Map>" + "<Extension />" + "</MapGroup>" + "<ddd>" + "33333333"
        + "</ddd>" + "<ddd>" + "444" + "</ddd>" + "</MapSet>"));

    String xml =
        "<body>   <p:helloXsl xmlns:p=\"http://ode/bpel/unit-test.wsdl\">      <!--Exactly 1 occurrence-->      <TestPart>         <!--Exactly 1 occurrence-->         <content>fdsafasdfasdf</content>      </TestPart>   </p:helloXsl></body>";

    System.out.println(Xml2JsonUtil.xml2JSON(xml));

    xml =
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:unit=\"http://ode/bpel/unit-test.wsdl\">   <soapenv:Header/>   <soapenv:Body>      <unit:helloXsl>         <TestPart>            <content>?</content>         </TestPart>      </unit:helloXsl>   </soapenv:Body></soapenv:Envelope>";

    System.out.println(Xml2JsonUtil.xml2JSON(xml));

    xml =
        "<ns:uploadServiceResponse xmlns:ns=\"http://services.deployer.bpel.carbon.wso2.org\"><ns:return xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/></ns:uploadServiceResponse>";

    System.out.println(Xml2JsonUtil.xml2JSON(xml));

    xml =
        "<body>   <p:planInput xmlns:p=\"http://www.open-o.org/tosca/nfv/2015/12\">      <!--Exactly 1 occurrence-->      <p:sfc_count>2</p:sfc_count>      <!--Exactly 1 occurrence-->      <p:iaUrl></p:iaUrl>      <!--Exactly 1 occurrence-->      <p:vnfmId>112</p:vnfmId>      <!--Exactly 1 occurrence-->      <p:object_context>{\"e\":{\"f\":\"4\"}}</p:object_context>      <!--Exactly 1 occurrence-->      <p:statusUrl></p:statusUrl>      <!--Exactly 1 occurrence-->      <p:serviceTemplateId>?</p:serviceTemplateId>      <!--Exactly 1 occurrence-->      <p:roUrl></p:roUrl>      <!--Exactly 1 occurrence-->      <p:vl_count>2</p:vl_count>      <!--Exactly 1 occurrence-->      <p:containerapiUrl>?</p:containerapiUrl>      <!--Exactly 1 occurrence-->      <p:flavor></p:flavor>      <!--Exactly 1 occurrence-->      <p:nsInstanceId>223</p:nsInstanceId>      <!--Exactly 1 occurrence-->      <p:instanceId>334</p:instanceId>      <!--Exactly 1 occurrence-->      <p:resourceUrl></p:resourceUrl>      <!--Exactly 1 occurrence-->      <p:vnf_count>2</p:vnf_count>      <!--Exactly 1 occurrence-->      <p:callbackId></p:callbackId>      <!--Exactly 1 occurrence-->      <p:object_additionalParamForVnf>[{\"b\":1},{\"c\":{\"d\":\"2\"}}}]</p:object_additionalParamForVnf>      <!--Exactly 1 occurrence-->      <p:object_additionalParamForNs>[{\"a\":3},{\"e\":{\"f\":\"4\"}}}]</p:object_additionalParamForNs>      <!--Exactly 1 occurrence-->      <p:flavorParams></p:flavorParams>   </p:planInput></body>";

    System.out.println(Xml2JsonUtil.xml2JSON(xml));
  }
}

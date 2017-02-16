/**
 * Copyright 2017 ZTE Corporation.
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
package org.openo.log.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Facitility {
  private static final Logger LOGGER = LoggerFactory.getLogger(Facitility.class.getName());

  private Facitility() {

  }

  public static String oToJ(Object o) {
    ObjectMapper om = new ObjectMapper();
    Writer w = new StringWriter();
    String json = null;
    try {

      om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
      om.writeValue(w, o);
      json = w.toString();
      w.close();
    } catch (IOException e) {
      LOGGER.error("IOException", e);
    }
    return json;
  }

  public static Map<String, String> readJson2Map(String json) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      Map<String, String> maps = objectMapper.readValue(json, Map.class);
      return maps;
    } catch (Exception e) {
      LOGGER.error("IOException", e);
      return null;
    }
  }

  public static String hashMapToJson(HashMap<String, String> map) {
    String string = "{";
    for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
      Entry e = (Entry) it.next();
      string += "\"" + e.getKey() + "\":";
      string += "\"" + e.getValue() + "\",";
    }
    string = string.substring(0, string.lastIndexOf(","));
    string += "}";
    return string;
  }

  public static String dateFormat(Date date) {
    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    return time.format(date);
  }

  public static String checkRequiredParam(HashMap<String, Object> checkParamsMap) {
    StringBuilder errMsg = new StringBuilder();
    java.util.Iterator<String> hashMapIt = checkParamsMap.keySet().iterator();
    int count = 0;
    while (hashMapIt.hasNext()) {
      String key = hashMapIt.next();
      Object value = checkParamsMap.get(key);
      if (value == null || "".equals(value)) {
        errMsg.append(key);
        count++;
        if (count < checkParamsMap.size() - 1) {
          errMsg.append(" and ");
        }
      }

    }
    if (count > 0) {
      errMsg.append(" can't be null or \"\"!\n ");
    }
    return errMsg.toString();
  }

  public static String checkRequiredJsonParam(String jsonParam, String key) {
    return "";
//    StringBuilder errMsg = new StringBuilder();
//    try {
//      ObjectMapper mapper = new ObjectMapper();
//      JsonNode jsonValue;
//
//
//      jsonValue = mapper.readTree(jsonParam.toString());
//      Iterator<Entry<String, JsonNode>> elements = jsonValue.fields();
//      while (elements.hasNext()) {
//        Entry<String, JsonNode> node = elements.next();
//        String childValue = node.getValue().asText();
//        if (childValue == null || "".equals(childValue)) {
//          errMsg.append(
//              "Both Chinese and English descriptions of this field cannot be empty: " + key + "/n");
//          break;
//        }
//      }
//
//      return errMsg.toString();
//    } catch (JsonProcessingException e) {
//      // TODO Auto-generated catch block
//      LOGGER.error("JsonProcessingException" , e);
//      return errMsg.toString();
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      LOGGER.error("IOException" , e);
//      return errMsg.toString();
//    }
  }
}

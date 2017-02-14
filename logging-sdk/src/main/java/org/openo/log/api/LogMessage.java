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
package org.openo.log.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;

import org.openo.log.impl.Facitility;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Huabing Zhao
 *
 */

public class LogMessage implements Serializable {
  private static final Logger LOGGER = LoggerFactory.getLogger(LogMessage.class.getName());

  protected long id = -1;


  protected String detail = null;

  protected String hostname = "";



  private String logType = "";


  protected HashMap<String, Object> extendedFields = null;

  private HashMap<String, String> localeMap = null;



  private static final long serialVersionUID = 200408041651009L;

  public String getDetail() {
    return detail;
  }


  public long getId() {
    return id;
  }

  public String getHostname() {
    return hostname;
  }



  public LogMessage() {}



  public String getLogType() {
    return logType;
  }

  public void setLogType(String logType) {
    this.logType = logType;
  }

  public HashMap<String, Object> getExtendedFields() {
    return extendedFields;
  }

  public void setExtendedFields(HashMap<String, Object> extendedFields) {
    this.extendedFields = extendedFields;
  }

  public HashMap<String, String> getLocaleMap() {
    return this.localeMap;
  }

  public void setLocalMap(HashMap<String, String> localeMap) {
    this.localeMap = localeMap;
  }

  protected void set(String condName, Object condValue) {
    if (condValue != null && !"".equals(condValue)) {
      if (localeMap == null) {
        localeMap = new HashMap<String, String>();
      }
      localeMap.put(condName, condValue.toString());

    }
  }


  protected void setLocale(String condName, String condValue) {

    if (condValue != null && !"".equals(condValue)) {
      if (localeMap == null) {
        localeMap = new HashMap<String, String>();
      }



      Map<String, String> mapJson = Facitility.readJson2Map(condValue);
      for (Entry<String, String> entry : mapJson.entrySet()) {
        String key = entry.getKey();

        String value = entry.getValue().replace("\"", "\'");
        localeMap.put(condName + "_" + key, value);
      }
    }

  }


}

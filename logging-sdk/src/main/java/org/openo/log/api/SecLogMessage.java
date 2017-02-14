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

import java.util.Date;

import org.openo.log.impl.Facitility;
import org.openo.log.impl.LogConst;

public class SecLogMessage extends LogMessage {

  private static final long serialVersionUID = 201603100924009L;


  public SecLogMessage() {
    super();
    this.setLogType(LogConst.SERLOG_TYPE);
  }


  public SecLogMessage(String userName, String hostname, String detail, String logName,
      Date logDate) {
    super();
    this.setLogType(LogConst.SERLOG_TYPE);

    this.userName = userName;
    this.set("userName", userName);

    this.hostname = hostname;
    this.set("hostname", hostname);

    this.logName = logName;
    this.setLocale("logName", logName);

    this.detail = detail;
    this.setLocale("detail", detail);

    this.logDate = logDate;
    this.set("logDate", Facitility.dateFormat(logDate));
  }

  protected String userName = "";


  protected String logName = null;


  protected String connectMode = "";


  protected String systemID = "";

  protected String mac = "";

  public String getMac() {
    return mac;
  }

  public SecLogMessage setMac(String mac) {
    this.set("mac", mac);
    this.mac = mac;
    return this;
  }


  protected Date logDate = null;


  public String getUserName() {

    return userName;
  }


  public SecLogMessage setUserName(String userName) {
    this.set("userName", userName);
    this.userName = userName;
    return this;
  }


  public String getConnectMode() {
    return connectMode;
  }


  public SecLogMessage setConnectMode(String connectMode) {
    this.set("connectMode", connectMode);
    this.connectMode = connectMode;
    return this;
  }

  public String getLogName() {
    return logName;
  }

  public SecLogMessage setLogName(String logName) {
    this.setLocale("logName", logName);
    this.logName = logName;
    return this;
  }

  public Date getLogDate() {
    return logDate;
  }

  public SecLogMessage setLogDate(Date logDate) {
    this.set("logDate", Facitility.dateFormat(logDate));
    this.logDate = logDate;
    return this;
  }

  public SecLogMessage setHostname(String hostname) {
    this.set("hostname", hostname);
    this.hostname = hostname;
    return this;
  }


  public SecLogMessage setId(long id) {
    this.set("id", id);
    this.id = id;
    return this;
  }


  public SecLogMessage setDetail(String detail) {
    this.detail = detail;
    this.setLocale("detail", detail);
    return this;
  }

}

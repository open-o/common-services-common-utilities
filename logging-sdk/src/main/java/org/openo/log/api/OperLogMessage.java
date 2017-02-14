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



public class OperLogMessage extends LogMessage {
  private static final long serialVersionUID = 200408041704009L;


  public OperLogMessage() {
    super();
    this.setLogType(LogConst.OPERLOG_TYPE);
  }


  public OperLogMessage(String userName, String operation, String descriptionInfo, String hostname,
      String operateResult, String rank, Date logStartDate, Date logEndDate, String detail) {
    super();
    this.setLogType(LogConst.OPERLOG_TYPE);

    this.userName = userName;
    this.set("userName", userName);

    this.descriptionInfo = descriptionInfo;
    this.setLocale("descriptionInfo", descriptionInfo);

    this.operation = operation;
    this.setLocale("operation", operation);

    this.logStartDate = logStartDate;
    this.set("logStartDate", Facitility.dateFormat(logStartDate));

    this.logEndDate = logEndDate;
    this.set("logEndDate", Facitility.dateFormat(logEndDate));

    this.hostname = hostname;
    this.set("hostname", hostname);

    this.operateResult = operateResult;
    this.set("operateResult", operateResult);

    this.rank = rank;
    this.set("rank", rank);

    this.detail = detail;
    this.setLocale("detail", detail);
  }


  protected String userName = "";


  protected String operation = null;


  protected String descriptionInfo = null;


  private String operationType = "other";

  protected String failReason = null;


  protected String operateResult = LogService.OPERLOG_SUCCESS;


  protected String connectMode = LogService.CONNECT_TYPE_WEB;


  protected String[] resourceIP;


  protected Date logStartDate = null;


  protected Date logEndDate = null;


  protected long linkId = -100;

  protected int isDisplay = LogService.LOG_DISPLAY;

  protected String mac = "";

  /**
   * name(id)|name(id)
   */
  private String[] operateResource = null;


  protected String rank = LogService.OPERLOG_RANK_NORMAL;

  public String getMac() {
    return mac;
  }

  public OperLogMessage setMac(String mac) {
    this.set("mac", mac);
    this.mac = mac;
    return this;
  }


  public String getUserName() {
    return userName;
  }

  public OperLogMessage setUserName(String name) {
    this.userName = name;
    this.set("userName", name);
    return this;
  }

  public String getFailReason() {
    return failReason;
  }

  public OperLogMessage setFailReason(String reason) {
    this.setLocale("failReason", reason);
    this.failReason = reason;
    return this;
  }

  public String getConnectMode() {
    return connectMode;

  }

  public OperLogMessage setConnectMode(String mode) {
    this.set("connectMode", mode);
    this.connectMode = mode;
    return this;
  }

  public long getLinkId() {
    return linkId;
  }

  public OperLogMessage setLinkId(long linkId) {
    this.set("linkId", linkId);
    this.linkId = linkId;
    return this;
  }

  public String getOperateResult() {
    return operateResult;
  }

  public OperLogMessage setOperateResult(String operateResult) {
    this.set("operateResult", operateResult);
    this.operateResult = operateResult;
    return this;
  }

  public String getRank() {
    return rank;
  }

  public OperLogMessage setRank(String rank) {
    this.set("rank", rank);
    this.rank = rank;
    return this;
  }

  public String getDescriptionInfo() {
    return descriptionInfo;
  }

  public OperLogMessage setDescriptionInfo(String desc) {
    this.setLocale("descriptionInfo", desc);
    descriptionInfo = desc;
    return this;
  }

  public String getOperation() {
    return operation;
  }

  public OperLogMessage setOperation(String operation) {
    this.setLocale("operation", operation);
    this.operation = operation;
    return this;
  }

  public String[] getResourceIP() {
    return resourceIP;
  }

  public OperLogMessage setResourceIP(String[] resourceIP) {
    this.set("resourceIP", resourceIP);
    this.resourceIP = resourceIP;
    return this;
  }

  public Date getLogStartDate() {
    return logStartDate;
  }

  public OperLogMessage setLogStartDate(Date logStartDate) {
    this.set("logStartDate", Facitility.dateFormat(logStartDate));
    this.logStartDate = logStartDate;
    return this;
  }

  public Date getLogEndDate() {
    return logEndDate;
  }

  public OperLogMessage setLogEndDate(Date logEndDate) {
    this.set("logEndDate", Facitility.dateFormat(logEndDate));
    this.logEndDate = logEndDate;
    return this;
  }

  public int getIsDisplay() {
    return isDisplay;
  }

  public OperLogMessage setIsDisplay(int isDisplay) {
    this.set("isDisplay", isDisplay);
    this.isDisplay = isDisplay;
    return this;
  }

  public String[] getOperateResource() {
    return operateResource;
  }

  public OperLogMessage setOperateResource(String[] operateResource) {
    this.set("operateResource", operateResource);
    this.operateResource = operateResource;
    return this;
  }

  public String getOperationType() {
    return operationType;
  }

  public OperLogMessage setOperationType(String operationType) {

    this.operationType = operationType;
    return this;
  }



  public OperLogMessage setHostname(String hostname) {
    this.set("hostname", hostname);
    this.hostname = hostname;
    return this;
  }


  public LogMessage setId(long id) {
    this.set("id", id);
    this.id = id;
    return this;
  }


  public LogMessage setDetail(String detail) {
    this.detail = detail;
    this.setLocale("detail", detail);
    return this;
  }

}

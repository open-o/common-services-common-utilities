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


public class SysLogMessage extends LogMessage {
  private static final long serialVersionUID = 200408041707009L;


  protected String rank = LogService.SYSLOG_RANK_INFORM;

  protected String source = null;

  protected String sourceId = "";

  protected String logName = null;


  protected Date logStartDate = null;

  protected Date logEndDate = null;

  protected int isDisplay = LogService.LOG_DISPLAY;

  protected long linkId = -100;


  public SysLogMessage() {
    super();
    this.setLogType(LogConst.SYSLOG_TYPE);
  }


  public SysLogMessage(String logName, String hostname, String rank, String detail,
      Date logStartDate, Date logEndDate) {
    super();
    this.setLogType(LogConst.SYSLOG_TYPE);

    this.logName = logName;
    this.set("logName", logName);

    this.rank = rank;
    this.set("rank", rank);

    this.hostname = hostname;
    this.set("hostname", hostname);

    this.detail = detail;
    this.set("detail", detail);

    this.logStartDate = logStartDate;
    this.set("logStartDate", Facitility.dateFormat(logStartDate));

    this.logEndDate = logEndDate;
    this.set("logEndDate", Facitility.dateFormat(logEndDate));
  }

  public Date getLogStartDate() {
    return logStartDate;
  }

  public SysLogMessage setLogStartDate(Date logStartDate) {
    this.set("logStartDate", Facitility.dateFormat(logStartDate));
    this.logStartDate = logStartDate;
    return this;
  }

  public Date getLogEndDate() {
    return logEndDate;
  }

  public SysLogMessage setLogEndDate(Date logEndDate) {
    this.set("logEndDate", Facitility.dateFormat(logEndDate));
    this.logEndDate = logEndDate;
    return this;
  }

  public String getRank() {
    return rank;
  }

  public SysLogMessage setRank(String rank) {
    this.set("rank", rank);
    this.rank = rank;
    return this;
  }

  public String getLogName() {
    return logName;
  }

  public SysLogMessage setLogName(String logName) {
    this.setLocale("logName", logName);
    this.logName = logName;
    return this;
  }


  public final String getSource() {
    return source;
  }


  public SysLogMessage setSource(String source) {
    this.setLocale("source", source);
    this.source = source;
    return this;
  }

  public int getIsDisplay() {
    return isDisplay;
  }

  public SysLogMessage setIsDisplay(int isDisplay) {
    this.set("isDisplay", isDisplay);
    this.isDisplay = isDisplay;
    return this;
  }

  public long getLinkId() {
    return linkId;
  }

  public String getSourceId() {
    return sourceId;
  }

  public SysLogMessage setSourceId(String sourceId) {
    this.set("sourceId", sourceId);
    this.sourceId = sourceId;
    return this;
  }

  public SysLogMessage setLinkId(long linkId) {
    this.set("linkId", linkId);
    this.linkId = linkId;
    return this;
  }

  public SysLogMessage setHostname(String hostname) {
    this.set("hostname", hostname);
    this.hostname = hostname;
    return this;
  }


  public SysLogMessage setId(long id) {
    this.set("id", id);
    this.id = id;
    return this;
  }


  public SysLogMessage setDetail(String detail) {
    this.detail = detail;
    this.setLocale("detail", detail);
    return this;
  }



}

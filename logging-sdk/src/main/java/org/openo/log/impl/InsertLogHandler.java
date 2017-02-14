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

import java.util.HashMap;
import org.slf4j.Logger;

import org.openo.log.api.LogMessage;
import org.openo.log.api.LogService;
import org.openo.log.api.OperLogMessage;
import org.openo.log.api.SecLogMessage;
import org.openo.log.api.SysLogMessage;
import org.slf4j.LoggerFactory;



public class InsertLogHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(InsertLogHandler.class.getName());

  public void insertLog(LogMessage msg, String logIndex, String logType) {
    String logJson = Facitility.hashMapToJson(msg.getLocaleMap());
    msg.setLocalMap(null);

    if (msg.getExtendedFields() != null) {
      String exendedFieldsJson = Facitility.oToJ(msg.getExtendedFields());
      logJson = logJson.substring(0, logJson.lastIndexOf("}")) + ","
          + exendedFieldsJson.substring(1) + "\n";
      msg.setExtendedFields(null);
    }
    LOGGER.info(logJson, logIndex, logType);


  }

  public boolean checkCmdLog(OperLogMessage msg) {
    StringBuilder errMsg = new StringBuilder();
    boolean errFlag = false;
    HashMap<String, Object> checkParamsMap = new HashMap<>();
    checkParamsMap.put("userName", msg.getUserName());
    checkParamsMap.put("operation", msg.getOperation());
    checkParamsMap.put("descriptionInfo", msg.getDescriptionInfo());
    checkParamsMap.put("hostname", msg.getHostname());
    checkParamsMap.put("detail", msg.getDetail());
    checkParamsMap.put("logStartDate", msg.getLogStartDate());
    checkParamsMap.put("logEndDate", msg.getLogEndDate());
    errMsg.append(Facitility.checkRequiredParam(checkParamsMap));


    errMsg.append(checkCmdRank(msg.getRank()));
    errMsg.append(checkConnectMode(msg.getConnectMode()));
    errMsg.append(checkOperateResult(msg.getOperateResult()));
    errMsg.append(checkIsDisplay(msg.getIsDisplay()));

    if (msg.getOperation() != null && !("").equals(msg.getOperation())) {
      errMsg.append(Facitility.checkRequiredJsonParam(msg.getOperation(), "operation"));
    }
    if (msg.getDescriptionInfo() != null && !("").equals(msg.getDescriptionInfo())) {
      errMsg.append(Facitility.checkRequiredJsonParam(msg.getDescriptionInfo(), "descriptionInfo"));
    }
    if (msg.getDetail() != null && !("").equals(msg.getDetail())) {
      errMsg.append(Facitility.checkRequiredJsonParam(msg.getDetail(), "detail"));
    }
    if (msg.getFailReason() != null && !("").equals(msg.getFailReason())) {
      errMsg.append(Facitility.checkRequiredJsonParam(msg.getFailReason(), "failReason"));
    }
    if (!"".equals(errMsg.toString())) {
      LOGGER.warn(errMsg.toString());
      printCmdLog(msg);
      errFlag = true;
    }
    return !errFlag;
  }

  private String checkIsDisplay(int isDisplay) {
    StringBuilder errMsg = new StringBuilder();
    if (isDisplay != LogService.LOG_DISPLAY && isDisplay != LogService.LOG_DISPLAY_NOT) {
      errMsg.append("isDisplay must be 'LogService.LOG_DISPLAY'");
      errMsg.append(" or 'LogService.LOG_DISPLAY_NOT'\n");

    }
    return errMsg.toString();
  }

  private String checkOperateResult(String operateResult) {
    StringBuilder errMsg = new StringBuilder();
    if ((operateResult != LogService.OPERLOG_SUCCESS)
        && (operateResult != LogService.OPERLOG_ERROR)) {
      errMsg.append("operateResult must be ' LogService.OPERLOG_ERROR'");
      errMsg.append(" or 'LogService.OPERLOG_SUCCESS'\n");

    }
    return errMsg.toString();
  }

  private String checkConnectMode(String connectMode) {
    StringBuilder errMsg = new StringBuilder();
    if (!(connectMode.equals(LogService.CONNECT_TYPE_WEB)
        || connectMode.equals(LogService.CONNECT_TYPE_TELNET)
        || connectMode.equals(LogService.CONNECT_TYPE_SSH)
        || connectMode.equals(LogService.CONNECT_TYPE_EM))) {
      errMsg.append("connectMode must be 'LogService.CONNECT_TYPE_WEB'");
      errMsg.append(" or 'LogService.CONNECT_TYPE_TELNET'");
      errMsg.append(" or 'LogService.CONNECT_TYPE_SSH'");
      errMsg.append(" or 'LogService.CONNECT_TYPE_EM'\n");

    }

    return errMsg.toString();
  }

  private String checkCmdRank(String rank) {
    StringBuilder errMsg = new StringBuilder();
    boolean isExist = false;
    String[] ranks = {LogService.OPERLOG_RANK_NOTICE, LogService.OPERLOG_RANK_NORMAL,
        LogService.OPERLOG_RANK_IMPORTANT, LogService.OPERLOG_RANK_VERYIMPORTANT};
    for (int i = 0; i < ranks.length; i++) {
      if (rank.equalsIgnoreCase(ranks[i])) {
        isExist = true;
        break;
      }
    }
    if (!isExist) {
      errMsg.append("rank must be 'LogService.OPERLOG_RANK_NORMAL'");
      errMsg.append(" or 'LogService.OPERLOG_RANK_NOTICE'");
      errMsg.append(" or 'LogService.OPERLOG_RANK_IMPORTANT'");
      errMsg.append(" or 'LogService.OPERLOG_RANK_VERYIMPORTANT'\n");

    }

    return errMsg.toString();
  }

  public void printCmdLog(OperLogMessage msg) {
    LOGGER.warn("CMD Log Error Detail msg");
    LOGGER.warn("UserName   :" + msg.getUserName());
    LOGGER.warn("operation         :" + msg.getOperation());
    LOGGER.warn("hostname :" + msg.getHostname());
    LOGGER.warn("descriptionInfo         :" + msg.getDescriptionInfo());
    LOGGER.warn("operateResult  :" + msg.getOperateResult());
    LOGGER.warn("FailReason :" + msg.getFailReason());
    LOGGER.warn("detail       :" + msg.getDetail());
    LOGGER.warn("ConnectMode:" + msg.getConnectMode());
    LOGGER.warn("OperateResource :" + msg.getOperateResource());
    LOGGER.warn("logStartDate :" + msg.getOperateResource());
    LOGGER.warn("logEndDate :" + msg.getLogEndDate());
    LOGGER.warn("Rank    :" + msg.getRank());
  }

  /**
   * æ£€æŸ¥æ—¥å¿—å�‚æ•°æ˜¯å�¦ç¬¦å�ˆè¦�æ±‚
   * 
   * @return true
   */
  public boolean checkSecLog(SecLogMessage msg) {
    StringBuilder errMsg = new StringBuilder();
    boolean errFlag = false;
    HashMap<String, Object> checkParamsMap = new HashMap<>();
    checkParamsMap.put("userName", msg.getUserName());
    checkParamsMap.put("logName", msg.getLogName());
    checkParamsMap.put("hostname", msg.getHostname());
    checkParamsMap.put("detail", msg.getDetail());
    checkParamsMap.put("logDate", msg.getLogDate());
    errMsg.append(Facitility.checkRequiredParam(checkParamsMap));
    errMsg.append(checkConnectMode(msg.getConnectMode()));

    if (msg.getLogName() != null && !("").equals(msg.getLogName())) {
      errMsg.append(Facitility.checkRequiredJsonParam(msg.getLogName(), "logName"));
    }
    if (msg.getDetail() != null && !("").equals(msg.getDetail())) {
      errMsg.append(Facitility.checkRequiredJsonParam(msg.getDetail(), "detail"));
    }
    if (!"".equals(errMsg.toString())) {
      LOGGER.warn(errMsg.toString());
      this.printSecLog(msg);
      errFlag = true;
    }
    return !errFlag;
  }

  public void printSecLog(SecLogMessage secLog) {
    LOGGER.warn("SCRT Log Error Detail msg");
    LOGGER.warn("logDate    :" + secLog.getLogDate());
    LOGGER.warn("logName    :" + secLog.getLogName());
    LOGGER.warn("userName   :" + secLog.getUserName());
    LOGGER.warn("hostname   :" + secLog.getHostname());
    LOGGER.warn("connectMode:" + secLog.getConnectMode());
    LOGGER.warn("detail   :" + secLog.getDetail());
  }

  public boolean checkSysLog(SysLogMessage msg) {
    StringBuilder errMsg = new StringBuilder();
    boolean errFlag = false;
    HashMap<String, Object> checkParamsMap = new HashMap<>();

    checkParamsMap.put("logName", msg.getLogName());
    checkParamsMap.put("logStartDate", msg.getLogStartDate());
    checkParamsMap.put("logEndDate", msg.getLogEndDate());
    checkParamsMap.put("hostname", msg.getHostname());
    checkParamsMap.put("detail", msg.getDetail());
    errMsg.append(Facitility.checkRequiredParam(checkParamsMap));
    errMsg.append(checkSysRank(msg.getRank()));
    if (msg.getLogName() != null && !("").equals(msg.getLogName())) {
      errMsg.append(Facitility.checkRequiredJsonParam(msg.getLogName(), "logName"));
    }
    if (msg.getDetail() != null && !("").equals(msg.getDetail())) {
      errMsg.append(Facitility.checkRequiredJsonParam(msg.getDetail(), "detail"));
    }
    if (!"".equals(errMsg.toString())) {
      LOGGER.warn(errMsg.toString());
      printSysLog(msg);
      errFlag = true;
    }
    return !errFlag;
  }

  private String checkSysRank(String rank) {
    StringBuilder errMsg = new StringBuilder();
    boolean isExist = false;
    String[] ranks = {LogService.SYSLOG_RANK_INFORM, LogService.SYSLOG_RANK_NOTICE,
        LogService.SYSLOG_RANK_WARN, LogService.SYSLOG_RANK_ERROR, LogService.SYSLOG_RANK_ALERT,
        LogService.SYSLOG_RANK_EMERG};
    for (int i = 0; i < ranks.length; i++) {
      if (rank.equalsIgnoreCase(ranks[i])) {
        isExist = true;
        break;
      }
    }
    if (!isExist) {
      errMsg.append("rank must be 'LogService.SYSLOG_INFORM'");
      errMsg.append(" or 'LogService.SYSLOG_NOTICE'");
      errMsg.append(" or 'LogService.SYSLOG_WARN'");
      errMsg.append(" or 'LogService.SYSLOG_ERROR'");
      errMsg.append(" or 'LogService.SYSLOG_ALTERT'");
      errMsg.append(" or 'LogService.SYSLOG_EMERG'\n");

    }

    return errMsg.toString();
  }

  public void printSysLog(SysLogMessage msg) {
    LOGGER.warn("System Log Error Detail msg");
    LOGGER.warn("logName :" + msg.getLogName());
    LOGGER.warn("rank    :" + msg.getRank());
    LOGGER.warn("source  :" + msg.getSource());
    LOGGER.warn("logStartDate :" + msg.getLogStartDate());
    LOGGER.warn("logEndDate :" + msg.getLogEndDate());
    LOGGER.warn("detail    :" + msg.getDetail());
  }

}


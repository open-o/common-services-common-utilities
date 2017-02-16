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

import org.openo.log.impl.InsertLogHandler;
import org.openo.log.impl.LogConst;
import org.openo.log.impl.LogIdTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class.getName());


  public static final String OPERLOG_SUCCESS = "log_success";

  public static final String OPERLOG_ERROR = "log_fail";


  public static final String OPERLOG_RANK_NORMAL = "operlog_rank_normal";

  public static final String OPERLOG_RANK_NOTICE = "operlog_rank_notice";

  public static final String OPERLOG_RANK_IMPORTANT = "operlog_rank_important";

  public static final String OPERLOG_RANK_VERYIMPORTANT = " operlog_rank_veryimportant";


  public static final String SYSLOG_RANK_INFORM = "syslog_rank_inform";

  public static final String SYSLOG_RANK_NOTICE = "syslog_rank_notice";


  public static final String SYSLOG_RANK_WARN = "syslog_rank_warn";

  public static final String SYSLOG_RANK_ERROR = "syslog_rank_error";


  public static final String SYSLOG_RANK_ALERT = "syslog_rank_alert";


  public static final String SYSLOG_RANK_EMERG = "syslog_rank_emerg";


  public static final String CONNECT_TYPE_WEB = "WEB";

  public static final String CONNECT_TYPE_TELNET = "TELNET";

  public static final String CONNECT_TYPE_SSH = "SSH";

  public static final String CONNECT_TYPE_EM = "EM";

  public static final short LOG_DISPLAY_NOT = 0;

  public static final short LOG_DISPLAY = 1;

  public static final int LOG_ROOT_LINK_ID = -1;

  private static final int LOG_NO_LINK_ID = 0;

  private static LogService recordLogHandler = null;

  public static LogService getInstance() {
    if (recordLogHandler == null) {
      recordLogHandler = new LogService();
    }
    return recordLogHandler;
  }

  public long recordOperLog(OperLogMessage logMessage) {
    // TODO Auto-generated method stub
    LOGGER.info("receive a insert operLog message");
    InsertLogHandler insertLogHandler = new InsertLogHandler();
    if (insertLogHandler.checkCmdLog((OperLogMessage) logMessage)) {

      long id =
          LogIdTool.getRandomID(LogConst.CMD_LOG_FLAG, logMessage.getLogStartDate().getTime());
      logMessage.setId(id);
      insertLogHandler.insertLog(logMessage, LogConst.AUDITLOG_OPERATION, LogConst.OPERLOG_TYPE);

      return logMessage.getId();
    }

    return 0;
  }

  public long recordSecLog(SecLogMessage logMessage) {
    // TODO Auto-generated method stub
    //LOGGER.info("receive a insert sec log message");
    InsertLogHandler insertLogHandler = new InsertLogHandler();
    if (insertLogHandler.checkSecLog((SecLogMessage) logMessage)) {

      long id = LogIdTool.getRandomID(LogConst.SECRET_LOG_FLAG, logMessage.getLogDate().getTime());
      logMessage.setId(id);

      insertLogHandler.insertLog(logMessage, LogConst.AUDITLOG_SECURITY, LogConst.SERLOG_TYPE);

      return logMessage.getId();
    }
    return 0;
  }



  public long recordSysLog(SysLogMessage logMessage) {
    //LOGGER.info("receive a insert sys log message");
    InsertLogHandler insertLogHandler = new InsertLogHandler();
    if (insertLogHandler.checkSysLog((SysLogMessage) logMessage)) {

      long id =
          LogIdTool.getRandomID(LogConst.SYS_LOG_FLAG, logMessage.getLogStartDate().getTime());
      logMessage.setId(id);
      insertLogHandler.insertLog(logMessage, LogConst.AUDITLOG_SYSTEM, LogConst.SYSLOG_TYPE);
      return logMessage.getId();
    }
    return 0;
  }

  public static void main(String[] args)
  {
    LogService service = new LogService();
    OperLogMessage logMessage = new OperLogMessage();
    
    service.recordOperLog(logMessage );
  }
}

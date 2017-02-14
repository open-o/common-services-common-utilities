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

public class LogConst {
  private LogConst() {

  }

  public final static int CMD_LOG_FLAG = 1;
  public final static int SYS_LOG_FLAG = 2;
  public final static int SECRET_LOG_FLAG = 3;

  public final static String AUDITLOG_OPERATION = "auditlog_operation";
  public final static String AUDITLOG_SECURITY = "auditlog_security";
  public final static String AUDITLOG_SYSTEM = "auditlog_system";

  public final static String OPERLOG_TYPE = "operlog";
  public final static String SERLOG_TYPE = "seclog";
  public final static String SYSLOG_TYPE = "syslog";



}

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

import java.util.Date;
import java.util.Random;



public class LogIdTool {
  private static Random cmdRand = new Random();

  private static Random sysRand = new Random();

  private static Random secRand = new Random();

  private LogIdTool() {

  }

  public static long getRandomID(int logType, long startDate) {
    String strCtime = "" + startDate;
    if (strCtime.length() > 13) {
      strCtime = strCtime.substring(strCtime.length() - 13);
    }
    int seed1;
    if (logType == LogConst.CMD_LOG_FLAG) {
      seed1 = cmdRand.nextInt(100000);
    } else if (logType == LogConst.SYS_LOG_FLAG) {
      seed1 = sysRand.nextInt(100000);
    } else if (logType == LogConst.SECRET_LOG_FLAG) {
      seed1 = secRand.nextInt(100000);
    } else {
      return 0;
    }
    String seed2 = "" + seed1;
    int shouldFillLen = 5 - seed2.length();
    for (int i = 0; i < shouldFillLen; i++) {
      seed2 = "0" + seed2;
    }
    return Long.parseLong(strCtime + seed2);
  }

  public static long transTimeCond2ID(Date time, boolean isStart) {
    long timeLong = time.getTime();
    if (isStart) {
      timeLong = timeLong * 100000;
    } else {
      timeLong = timeLong * 100000 + 99999;
    }
    return timeLong;

  }
}

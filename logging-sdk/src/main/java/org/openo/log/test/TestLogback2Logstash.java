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
package org.openo.log.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.openo.log.api.LogService;
import org.openo.log.api.SysLogMessage;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * @author Huabing Zhao
 *
 */
public class TestLogback2Logstash {

  /**
   * @param args
   * @throws JoranException
   * @throws IOException
   * @throws InterruptedException
   */
  public static void main(String[] args) throws JoranException, IOException, InterruptedException {
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    loggerContext.reset();
    JoranConfigurator configurator = new JoranConfigurator();
    InputStream configStream = TestLogback2Logstash.class.getResourceAsStream("logback.xml");
    configurator.setContext(loggerContext);
    configurator.doConfigure(configStream); // loads logback file
    configStream.close();

    for (int i = 0; i < 10; i++) {
      SysLogMessage sysLogMessage = new SysLogMessage("a sys log example", "www.open-o.org",
          LogService.SYSLOG_RANK_WARN, "logdetails", new Date(), new Date());
      LogService.getInstance().recordSysLog(sysLogMessage);
    }
    loggerContext.stop();
  }
}

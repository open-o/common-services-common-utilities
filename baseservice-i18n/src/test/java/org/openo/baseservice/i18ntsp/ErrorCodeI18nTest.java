/**
 * Copyright 2017 ZTE Corporation.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.baseservice.i18ntsp;

import org.jvnet.hk2.annotations.Service;
import org.openo.baseservice.i18n.ErrorCodeI18n;
import org.openo.baseservice.i18n.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Service
public class ErrorCodeI18nTest {

  private static Logger logger = LoggerFactory.getLogger(ErrorCodeI18nTest.class);

  public static void main(String[] args) {
    ErrorCodeI18n.getInstance().getErrorItem(53530).ifPresent(errorCodeI18n -> {
      int level = errorCodeI18n.getLevel();
      System.out.println("getLevel is:" + level);
      logger.info("APITEST:I18N_getLevel_success!");
    }

    );
  }

  @Inject
  I18nService i18nService;

  @PostConstruct
  void errorCodeI18nTest() {
    // getErrorCodeTest();
    getLevelTest();
  }

  /*
   * private void getErrorCodeTest() {
   * 
   * i18nService.getErrorItem(00053530).ifPresent(errorCodeI18n -> { int code =
   * errorCodeI18n.getErrorCode(); System.out.println("getErrorCode is:" + code);
   * logger.info("APITEST:I18N_getErrorCode_success!"); }
   * 
   * ); }
   */
  private void getLevelTest() {
    ErrorCodeI18n.getInstance().getErrorItem(53530).ifPresent(errorCodeI18n -> {
      int level = errorCodeI18n.getLevel();
      System.out.println("getLevel is:" + level);
      logger.info("APITEST:I18N_getLevel_success!");
    }

    );

  }

}

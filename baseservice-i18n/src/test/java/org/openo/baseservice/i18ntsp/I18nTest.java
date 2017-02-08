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
/**
package com.zte.ums.zenap.i18ntsp;


import com.zte.ums.zenap.i18n.I18n;
import com.zte.ums.zenap.i18n.I18nService;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class I18nTest {

  private static Logger logger = LoggerFactory.getLogger(I18nTest.class);

  @Inject
  I18nService i18nService;

  @PostConstruct
  void i18nTest() {
    getCanonicalLabelValuesTest();
    getI18nTest();
    getLabelValuesTest();
    getLabelValueTest();
    getLabelValuesKeyTest();

  }

  private void getCanonicalLabelValuesTest() {
    i18nService.getI18n("topo").ifPresent(i18n -> {
      String values = i18n.getCanonicalLabelValues("uca");
      System.out.println("getCanonicalLabelValues is:" + values);
      if (values.equals(
          "{\"ru_ru\":\"Единый общий приложений\",\"en_us\":\"Unified common application\",\"zh_cn\":\"统一公共应用\"}")) {
        logger.info("APITEST:I18N_getCanonicalLabelValues_success!");
      } else {
        logger.info("APITEST:i18N_getCanonicalLabelValues_failed!");
      }

      String value = I18n.getValuefromCanonicalValues(values, new Locale("zh_CN"));
      System.out.println("getValuefromCanonicalValues is:" + value);
      if (value.equals("统一公共应用")) {
        logger.info("APITEST:I18N_getValuefromCanonicalValues_success!");
      } else {
        logger.info("APITEST:I18N_getValuefromCanonicalValues_failed!");
      }

    });
  }

  private void getI18nTest() {

    i18nService.getI18n("topo").ifPresent(i18n -> {
      String key = i18n.getKeyFromValue("统一公共应用");
      System.out.println("统一公共应用's key is:" + key);
      if (key.equals("uca")) {
        logger.info("APITEST:I18N_getKeyFromValue_success!");
      } else {
        logger.info("APITEST:I18N_getKeyFromValue_failed!");
      }
    });

  }

  private void getLabelValueTest() {

    I18n.getInstance("topo").ifPresent(i18n -> {
      String b = i18n.getLabelValue("uca", new Locale("zh_CN"));
      System.out.println("uca is:" + b);
      if (b.equals("统一公共应用")) {
        logger.info("APITEST:I18N_getLabelValue_success!");
      } else {
        logger.info("APITEST:I18N_getLabelValue_failed!");
      }

    });

  }

  private void getLabelValuesKeyTest() {

    I18n.getInstance("topo").ifPresent(i18n -> {
      Map<String, String> map2 = i18n.getLabelValues("uca");
      for (Entry<String, String> entry : map2.entrySet()) {
        String locale = entry.getKey();
        String value = entry.getValue();
        // System.out.println("mmmmmmm:" + locale);
        // System.out.println("mmmmmmm:" + value);

        switch (locale) {
          case "ru_ru":
            if (value.equals("Единый общий приложений")) {
              logger.info("APITEST:I18N_getLabelValues_ru_1_success!");
              break;
            } else {
              logger.info("APITEST:I18N_getLabelValues_ru_1_failed!");
              break;
            }

          case "en_us":
            if (value.equals("Unified common application")) {
              logger.info("APITEST:I18N_getLabelValues_en_1_success!");
              break;
            } else {
              logger.info("APITEST:I18N_getLabelValues_en_1_failed!");
              break;
            }

          case "zh_cn":
            if (value.equals("统一公共应用")) {
              logger.info("APITEST:I18N_getLabelValues_zh_1_success!");
              break;
            } else {
              logger.info("APITEST:I18N_getLabelValues_zh_1_failed!");
              break;
            }

        }

      }

    });

  }

  private void getLabelValuesTest() {

    i18nService.getI18n("topo").ifPresent(i18n -> {
      String[] a = new String[] {"12", "12", "12"};
      Map<String, String> map = i18n.getLabelValues("col_dir_file_time_table", a);
      for (Entry<String, String> entry : map.entrySet()) {
        String locale = entry.getKey();
        String value = entry.getValue();
        System.out.println("aaaaaaa:" + locale);
        System.out.println("aaaaaaa:" + value);
        switch (locale) {
          case "ru_ru":
            if (value.equals("Дней сохранения мелких12мелких")) {
              logger.info("APITEST:I18N_getLabelValues_ru_2_success!");
              break;
            } else {
              logger.info("APITEST:I18N_getLabelValues_ru_2_failed!");
              break;
            }

          case "en_us":
            if (value.equals("file save days12days")) {
              logger.info("APITEST:I18N_getLabelValues_en_2_success!");
              break;
            } else {
              logger.info("APITEST:I18N_getLabelValues_en_2_failed!");
              break;
            }

          case "zh_cn":
            if (value.equals("文件保存天数12天")) {
              logger.info("APITEST:I18N_getLabelValues_zh_2_success!");
              break;
            } else {
              logger.info("APITEST:I18N_getLabelValues_zh_2_failed!");
              break;
            }

        }

      }

    });

  }

}
*/
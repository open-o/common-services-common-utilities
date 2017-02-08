/**
 * Copyright 2016 ZTE Corporation.
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
package org.openo.baseservice.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Consumer;

public class I18nTest {
  private static Logger logger = LoggerFactory.getLogger(I18nTest.class);

  public static void main(String[] args) {
    I18n.getInstance("topo").ifPresent(i18n -> {
      String values = i18n.getCanonicalLabelValues("uca");
      System.out.println(values);
      String value = I18n.getValuefromCanonicalValues(values, new Locale("zh", "CN"));
      System.out.println(value);
      System.out.println(i18n.getKeyFromValue("统一公共应用"));
    });

    I18n.getInstance("topo").ifPresent(new Consumer<I18n>() {
      @Override
      public void accept(I18n i18n) {
        String values = i18n.getCanonicalLabelValues("uca");
        String value = I18n.getValuefromCanonicalValues(values, new Locale("zh", "CN"));
        System.out.println(value);
        System.out.println(i18n.getKeyFromValue("统一公共应用"));
      }
    });

    I18n topo = I18n.getInstance("topo1").orElse(null);

    System.exit(0);
  }
}

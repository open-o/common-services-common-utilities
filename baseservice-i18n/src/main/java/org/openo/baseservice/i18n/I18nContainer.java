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
package org.openo.baseservice.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

final class I18nContainer {

  private static Logger logger = LoggerFactory.getLogger(I18nContainer.class);

  private Map<String, I18n> i18ns;

  private I18nContainer() {
    init();
  }


  private void init() {
    Map<String, Map<String, I18nItem>> i18nTemps = generateI18ns();

    i18ns = new HashMap<String, I18n>();
    for (Entry<String, Map<String, I18nItem>> entry : i18nTemps.entrySet()) {
      String name = entry.getKey();
      Map<String, I18nItem> items = entry.getValue();
      for (Entry<String, I18nItem> i18nItemEntry : items.entrySet()) {
        i18nItemEntry.getValue().unmodifiable();
      }

      I18n i18n = new I18nImpl(name, items);
      i18ns.put(name, i18n);
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, Map<String, I18nItem>> generateI18ns() {
    Map<String, Map<String, I18nItem>> i18nTemps = new HashMap<String, Map<String, I18nItem>>();

    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
    JsonResourceScanner.findI18nPaths().forEach((path) -> {
      HashMap<String, String> fileValues = null;
      try (InputStream ins = systemClassLoader.getResourceAsStream(path)) {
        fileValues = I18nJsonUtil.getInstance().readFromJson(ins, HashMap.class);
        logger.info("load i18n file success: " + path);
      } catch (IOException ex) {
        logger.info("load i18n file failed: " + path);
        logger.info("load i18n file failed: " + systemClassLoader.getResource(path).toString(), ex);
        return;
      }

      if (!validateI18nFileValues(fileValues, path)) {
        return;
      }

      String fileName = null;
      int i = path.lastIndexOf("/");
      if (i > -1) {
        fileName = path.substring(i + 1);
      } else {
        fileName = path;
      }
      i = fileName.indexOf("-i18n-");
      String name = fileName.substring(0, i);
      String localeSrc = fileName.substring(i + 6, fileName.lastIndexOf("."));
      if (name.isEmpty()) {
        logger.info("parse i18n file failed: name is null");
        return;
      } else if (localeSrc.isEmpty()) {
        logger.info("parse i18n file failed: locale is null");
        return;
      }

      String[] ss = localeSrc.replace("-", "_").split("_");
      String locale = null;
      if (ss.length == 1) {
        locale = new Locale(ss[0]).toString();
      } else if (ss.length == 2) {
        locale = new Locale(ss[0], ss[1]).toString();
      } else {
        logger.info("parse i18n file failed: locale is error \"" + localeSrc + "\"");
        return;
      }

      Map<String, I18nItem> i18nItems = i18nTemps.get(name);
      if (i18nItems == null) {
        i18nItems = new HashMap<String, I18nItem>();
        i18nTemps.put(name, i18nItems);
      }

      for (Entry<String, String> entry : fileValues.entrySet()) {
        String label = entry.getKey();

        I18nItem i18nItem = i18nItems.get(label);
        if (i18nItem == null) {
          i18nItem = new I18nItem(label);
          i18nItems.put(label, i18nItem);
        }

        i18nItem.addValue(locale, entry.getValue());
      }
    });
    return i18nTemps;
  }

  private boolean validateI18nFileValues(HashMap<String, String> fileValues, String path) {
    for (Entry<String, String> entry : fileValues.entrySet()) {
      if (entry.getValue() != null && !String.class.isInstance(entry.getValue())) {
        logger.info("parse i18n file failed: " + path + " field's[" + entry.getKey()
            + "] value is not string type");
        return false;
      }
    }
    return true;
  }

  protected static I18nContainer getInstance() {
    return I18nContainerSingleton.singleton;
  }

  Optional<I18n> getI18n(String name) {
    return Optional.ofNullable(this.i18ns.get(name));
  }

  static class I18nContainerSingleton {
    private static final I18nContainer singleton = new I18nContainer();
  }
}

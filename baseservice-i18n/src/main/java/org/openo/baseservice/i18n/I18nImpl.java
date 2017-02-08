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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class I18nImpl implements I18n {
  private static Logger logger = LoggerFactory.getLogger(I18nImpl.class);
  /**
   * 国际化文件前缀
   */
  private String name;

  /**
   * Map<键的名称，国际化项>
   */
  private Map<String, I18nItem> items;

  private Map<String, String> keyToValueMap;

  private Map<String, String> valueToKeyMap;


  public I18nImpl(String name, Map<String, I18nItem> items) {
    this.name = name;
    this.items = Collections.unmodifiableMap(items);

    keyToValueMap = new HashMap<>(items.size());
    valueToKeyMap = new HashMap<>(items.size() * 3);

    try {
      for (Entry<String, I18nItem> entry : items.entrySet()) {
        String key = entry.getKey();
        I18nItem item = entry.getValue();
        String value = I18nJsonUtil.getInstance().writeToJson(item.getValues());

        keyToValueMap.put(key, value);
        for (Entry<String, String> valueEntry : item.getValues().entrySet()) {
          valueToKeyMap.put(valueEntry.getValue(), key);
        }
      }
    } catch (Exception e) {
      logger.error("new I18nImpl failed:" + name, e);
    }

    keyToValueMap = Collections.unmodifiableMap(keyToValueMap);
    valueToKeyMap = Collections.unmodifiableMap(valueToKeyMap);
  }

  @Override
  public Map<String, String> getLabelValues(String labelKey) {
    I18nItem item = items.get(labelKey);
    if (item != null) {
      return item.getValues();
    }
    return null;
  }

  @Override
  public String getLabelValue(String labelKey, Locale theLocale) {
    I18nItem item = items.get(labelKey);
    if (item != null) {
      Map<String, String> values = item.getValues();
      return values.get(I18nLocaleTransfer.transfer(theLocale, values.keySet()));
    }
    return null;
  }

  @Override
  public String getLabelValue(String labelKey, Locale theLocale, String[] arguments) {
    I18nItem item = items.get(labelKey);
    if (item != null) {
      Map<String, String> values = item.getValues();
      String value = values.get(I18nLocaleTransfer.transfer(theLocale, values.keySet()));
      return replaceArguments(value, arguments);
    }
    return null;
  }

  @Override
  public Map<String, String> getLabelValues(String labelKey, String[] arguments) {
    I18nItem item = items.get(labelKey);
    if (item != null) {
      Map<String, String> map = new HashMap<String, String>();
      for (Entry<String, String> entry : item.getValues().entrySet()) {
        String value = entry.getValue();
        map.put(entry.getKey(), replaceArguments(value, arguments));
      }
      return map;
    }
    return null;
  }

  @Override
  public String getCanonicalLabelValues(String labelKey) {
    return keyToValueMap.get(labelKey);
  }

  @Override
  public String getKeyFromValue(String aValue) {
    return valueToKeyMap.get(aValue);
  }

  /**
   * 替换值中的参数
   * 
   * @param value 值
   * @param arguments 参数值
   * @return
   */
  private String replaceArguments(String value, String[] arguments) {
    if (value == null) {
      return null;
    }
    if (arguments != null) {
      int i = 0;
      for (String argument : arguments) {
        if (argument == null) {
          value = value.replaceAll("\\{\\s*" + i + "\\s*\\}", "");
        } else {
          value = value.replaceAll("\\{\\s*" + i + "\\s*\\}", argument);
        }
        i++;
      }
    }
    return value.replaceAll("\\{\\s*\\d+\\s*\\}", "");
  }

}

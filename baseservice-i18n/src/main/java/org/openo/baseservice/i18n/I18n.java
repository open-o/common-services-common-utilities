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
package org.openo.baseservice.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface I18n {
  static final Logger logger = LoggerFactory.getLogger(I18n.class);

  /**
   * Get the corresponding examples of international documents (all languages), <br>
   * 
   * for the above example topology (for example, the Chinese definition: topo-i18n-zh-CN.json,
   * English definition Topo -i18n-en-US.json), into the reference into the "TOPO" can be <br>
   * 
   * (i.e. except "-i18n-*.json" after the exact match).
   * 
   * @param i18nFilePrefix
   * @return Optional<I18n>
   */
  public static Optional<I18n> getInstance(String i18nFilePrefix) {
    return I18nContainer.getInstance().getI18n(i18nFilePrefix);
  }

  /**
   * According to key, the internationalization of all languages is described (Map<locale,value>)
   * 
   * @param labelKey key
   * @return Map<locale,value>
   */
  public Map<String, String> getLabelValues(String labelKey);

  /**
   * According to key, the internationalization of all languages is described
   * 
   * @param labelKey
   * @param theLocale
   * @return
   */
  public String getLabelValue(String labelKey, Locale theLocale);

  /**
   * According to key, the internationalization of all languages is described
   * 
   * @param labelKey
   * @param theLocale
   * @param arguments
   * @return
   */
  public String getLabelValue(String labelKey, Locale theLocale, String[] arguments);

  /**
   * Replace the placeholder that is defined in the internationalization description, as shown in
   * the following example, <br>
   * 
   * {0} represents first bits, which will be replaced by a specific number of days. : <br>
   * 
   * {"col_dir_file_time_table": "save days {0} days"}
   * 
   * @param labelKey
   * @param arguments
   * @return Map<locale,value>
   */
  public Map<String, String> getLabelValues(String labelKey, String[] arguments);

  /**
   * According to the key to get all the international description of the language, and the results
   * will be combined into a Json string, used in the log module scene, <br>
   * 
   * it is necessary to store all of the international information or transfer, and finally show the
   * time according to locale to choose the appropriate value. <br>
   * 
   * (here in order to form similar to read, plus a newline) <br>
   * {"zh_CN":"统一公共应用","en_US":" Unified common application ","ru_RU":"Единый общий приложений"}
   * 
   * @param labelKey key
   * @return
   */
  public String getCanonicalLabelValues(String labelKey);

  /**
   * With the use of getCanonicalLabelValues interface, from all the international description of
   * the combination of string and specific language corresponding to the international string. The
   * return value of the getCanonicalLabelValues method
   * 
   * @param values
   * @param theLocale
   * @return value
   */
  public static String getValuefromCanonicalValues(String values, Locale theLocale) {
    try {
      @SuppressWarnings("unchecked")
      HashMap<String, String> map = I18nJsonUtil.getInstance().readFromJson(values, HashMap.class);

      return map.get(I18nLocaleTransfer.transfer(theLocale, map.keySet()));
    } catch (Exception e) {
      logger.error("get i18n value failed by locale:" + theLocale + " from " + values, e);
      return null;
    }
  }

  /**
   * According to the value to find the corresponding key, such as a module in accordance with
   * international content filtering, search, database storage is key, then the front pass query is
   * a kind of language corresponding to the value, to get the corresponding international key to
   * the database query.
   *
   * @param value
   * @return
   */
  public String getKeyFromValue(String value);

}

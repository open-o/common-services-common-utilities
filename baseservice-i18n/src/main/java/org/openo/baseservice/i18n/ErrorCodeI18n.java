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

import org.openo.baseservice.i18n.DefaultErrorCodeI18n.ErrorCodeLevelUtil;
import org.openo.baseservice.i18n.DefaultErrorCodeI18n.ErrorItem2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface ErrorCodeI18n {

  static final Logger logger = LoggerFactory.getLogger(ErrorCodeI18n.class);

  /**
   * Access to the internationalization of the error code examples, scanning the process of all
   * classes load path, loading all the *-errorcode-*.json files, and load all the international
   * language information
   * 
   * @return
   */
  static ErrorCodeI18n getInstance() {
    return DefaultErrorCodeI18n.getInstance();
  }

  /**
   * Gets the corresponding error item based on the error code (including the error description
   * information for all languages)
   *
   * @param errorCode
   * @return Optional<ErrorItem>
   */
  public Optional<ErrorItem> getErrorItem(int errorCode);

  public static interface ErrorItem {

    public int getErrorCode();

    public int getLevel();

    public Map<String, String> getLabels();

    public String getLabel(Locale theLocale);

    /**
     * All language error information description of assembly. <br>
     * 
     * some modules to store all the error information description or transfer, and finally the time
     * to choose the appropriate value according to the locale presentation. <br>
     * 
     * it is not necessary to pass the error code (code), but in order to take into account the
     * subsequent scalability, so also passed, the general module does not need this information.
     * Similar form:<br>
     * { "code":53501, "level":"INFO", "errlabels":{"zh_CN":"拓扑定制文件无效。","en_US":"The topology
     * customized file is invalid.","ru_RU":"Топология настроенный файл недействительно."} }
     * 
     * @param errorCode
     * @return
     */
    public String getCanonicalLabels(int errorCode);

    /**
     * With the use of the above interface, it is possible to obtain an international string
     * corresponding to a specific language from the combination of all error messages
     * 
     * @param labels Error message description string (Return value of the getCanonicalLabels
     *        method)
     * @param theLocale
     * @return
     */
    public static String getLabelFromCanonicalLabels(String labels, Locale theLocale) {
      if (labels == null || theLocale == null) {
        return null;
      }
      try {
        ErrorItem2 errorItem2 = I18nJsonUtil.getInstance().readFromJson(labels, ErrorItem2.class);
        Map<String, String> errlabels = errorItem2.getErrlabels();
        return errlabels.get(I18nLocaleTransfer.transfer(theLocale, errlabels.keySet()));
      } catch (Exception e) {
        logger.info(
            "getLabelFromCanonicalLabels failed from " + labels + " with local " + theLocale, e);
        return null;
      }
    }

    /**
     * With the use of the above interface, we can get the error code corresponding to the specific
     * language from all the error information
     * 
     * @param labels Error message description string (Return value of the getCanonicalLabels
     *        method)
     * @param theLocale
     * @return errorCode
     */
    public static int getErrorcodeFromCanonicalLabels(String labels, Locale theLocale) {
      if (labels == null) {
        return -1;
      }
      try {
        ErrorItem2 errorItem2 = I18nJsonUtil.getInstance().readFromJson(labels, ErrorItem2.class);
        return errorItem2.getErrorCode();
      } catch (Exception e) {
        logger.info(
            "getErrorcodeFromCanonicalLabels failed from " + labels + " with local " + theLocale,
            e);
        return -1;
      }
    }

    /**
     * With the above interface, the error level of the corresponding string is obtained from all
     * the error messages
     *
     * @param labels Error message description string (Return value of the getCanonicalLabels
     *        method)
     * @param theLocale
     * @return error level
     */
    public static int getLevelFromCanonicalLabels(String labels, Locale theLocale) {
      if (labels == null) {
        return -1;
      }
      try {
        ErrorItem2 errorItem2 = I18nJsonUtil.getInstance().readFromJson(labels, ErrorItem2.class);
        return ErrorCodeLevelUtil.transfer2Int(errorItem2.getLevel());
      } catch (Exception e) {
        logger.info(
            "getErrorcodeFromCanonicalLabels failed from " + labels + " with local " + theLocale,
            e);
        return -1;
      }
    }
  }

}

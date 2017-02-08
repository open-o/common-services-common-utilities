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

import org.openo.baseservice.i18n.DefaultErrorCodeI18n.ErrorCodeLevelUtil;
import org.openo.baseservice.i18n.DefaultErrorCodeI18n.ErrorItem2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * 错误码国际化接口
 * 
 * @author 10163976
 *
 */
public interface ErrorCodeI18n {

  static final Logger logger = LoggerFactory.getLogger(ErrorCodeI18n.class);

  /**
   * 获取错误码国际化实例，扫描本进程所有类加载路径，加载所有的*-errorcode-*.json文件，并且加载所有语言的国际化信息
   * 
   * @return
   */
  static ErrorCodeI18n getInstance() {
    return DefaultErrorCodeI18n.getInstance();
  }

  /**
   * 根据错误码得到对应的错误项（包含所有语言的错误描述信息）
   * 
   * @param errorCode 错误码
   * @return Optional<ErrorItem>
   */
  public Optional<ErrorItem> getErrorItem(int errorCode);

  /**
   * 错误码类
   * 
   * @author 10163976
   */
  public static interface ErrorItem {

    /**
     * 获取 错误码
     * 
     * @return
     */
    public int getErrorCode();

    /**
     * 获取错误级别
     * 
     * @return
     */
    public int getLevel();

    /**
     * 获取错误信息描述
     * 
     * @return Map<locale ,value>
     */
    public Map<String, String> getLabels();

    /**
     * @param theLocale
     * @return
     */
    public String getLabel(Locale theLocale);

    /**
     * 得到规范组装的所有语言错误信息描述。<br>
     * 某些模块要将所有的错误信息描述都存储或者传递，最后呈现的时候再根据locale选择合适的值呈现。<br>
     * 原本无须传递错误码（code），但是为了考虑到后续扩展性，所以也传递了，一般模块不需要这个信息。形式类似：<br>
     * { "code":53501, "level":"INFO", "errlabels":{"zh_CN":"拓扑定制文件无效。","en_US":"The topology
     * customized file is invalid.","ru_RU":"Топология настроенный файл недействительно."} }
     * 
     * @param errorCode 错误码
     * @return
     */
    public String getCanonicalLabels(int errorCode);

    /**
     * 配合上面的接口使用，从所有的错误信息描述组合串中取得和特定语言对应的国际化串
     * 
     * @param labels 错误信息描述组合串 (getCanonicalLabels方法的返回值)
     * @param theLocale 语言
     * @return 国际化串
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
     * 配合上面的接口使用，从所有的错误信息描述组合串中取得和特定语言对应的错误码
     * 
     * @param labels 错误信息描述组合串 (getCanonicalLabels方法的返回值)
     * @param theLocale 语言
     * @return 错误码
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
     * 配合上面的接口使用，从所有的错误信息描述组合串中取得和特定语言对应的错误级别
     * 
     * @param labels 错误信息描述组合串 (getCanonicalLabels方法的返回值)
     * @param theLocale 语言
     * @return 错误级别
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

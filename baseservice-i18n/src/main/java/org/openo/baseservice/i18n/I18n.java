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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * 国际化信息接口
 * 
 * @author 10163976
 *
 */
public interface I18n {
  static final Logger logger = LoggerFactory.getLogger(I18n.class);

  /**
   * 得到国际化文件对应的实例（所有语言）,<br>
   * 对于上面例子的拓扑 （比如中文定义：topo-i18n-zh-CN.json，英文定义topo -i18n-en-US.json），入参传入“topo”即可<br>
   * （即除开"-i18n-*.json"后做精确匹配）。
   * 
   * @param i18nFilePrefix 国际化文件前缀(-i18n-*.json前面部分)
   * @return Optional<I18n>
   */
  public static Optional<I18n> getInstance(String i18nFilePrefix) {
    return I18nContainer.getInstance().getI18n(i18nFilePrefix);
  }

  /**
   * 根据key得到所有语言的国际化描述 (Map<locale,value>)
   * 
   * @param labelKey key
   * @return Map<locale,value>
   */
  public Map<String, String> getLabelValues(String labelKey);

  /**
   * 根据key得到对应语言的国际化描述
   * 
   * @param labelKey key
   * @param theLocale 方言
   * @return 描述信息
   */
  public String getLabelValue(String labelKey, Locale theLocale);

  /**
   * 根据key得到对应语言的国际化描述
   * 
   * @param labelKey key
   * @param theLocale 方言
   * @param arguments 参数值
   * @return 描述信息
   */
  public String getLabelValue(String labelKey, Locale theLocale, String[] arguments);

  /**
   * 替换国际化描述中定义的占位，占位示例如下，<br>
   * {0}表示第1个占位，会根据入参替换成具体的天数。：<br>
   * {"col_dir_file_time_table":"文件保存天数{0}天"}
   * 
   * @param labelKey key
   * @param arguments 参数值
   * @return Map<locale,value>
   */
  public Map<String, String> getLabelValues(String labelKey, String[] arguments);

  /**
   * 根据key得到所有语言的国际化描述，并且将结果组合成一个Json串，用在日志模块等场景，<br>
   * 其要将所有的国际化信息都存储或者传递，最后呈现的时候再根据locale选择合适的值呈现。<br>
   * 形式类似(这里为了好读，加了换行符）<br>
   * {"zh_CN":"统一公共应用","en_US":" Unified common application ","ru_RU":"Единый общий приложений"}
   * 
   * @param labelKey key
   * @return Map<locale,value>的json形式额字符串
   */
  public String getCanonicalLabelValues(String labelKey);

  /**
   * 配合getCanonicalLabelValues的接口使用，从所有的国际化描述组合串中取得和特定语言对应的国际化串。 入参为getCanonicalLabelValues方法的返回值
   * 
   * @param values 描述信息
   * @param theLocale 方言
   * @return value 对应方言的描述信息
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
   * 根据value找到对应的key， 比如某模块按照国际化内容过滤，查找，数据库中存的是key， 则前端传过来的查询条件是某种语言对应的value，要得到对应的国际化key再到数据库中查询。
   * 
   * @param value
   * @return
   */
  public String getKeyFromValue(String value);

}

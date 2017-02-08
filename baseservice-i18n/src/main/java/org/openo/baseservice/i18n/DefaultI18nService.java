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


import com.fasterxml.jackson.databind.ObjectMapper;
import org.jvnet.hk2.annotations.Service;
import org.openo.baseservice.i18n.ErrorCodeI18n.ErrorItem;

import javax.inject.Inject;
import java.util.Optional;

@Service
public final class DefaultI18nService implements I18nService {

  /**
   * 得到国际化文件对应的实例（所有语言）,<br>
   * 对于上面例子的拓扑 （比如中文定义：topo-i18n-zh-CN.json，英文定义topo -i18n-en-US.json），入参传入“topo”即可<br>
   * （即除开"-i18n-*.json"后做精确匹配）。
   *
   * @param i18nFilePrefix 国际化文件前缀(-i18n-*.json前面部分)
   * @return Optional<I18n>
   */
  public Optional<I18n> getI18n(String i18nFilePrefix) {
    return I18nContainer.getInstance().getI18n(i18nFilePrefix);
  }

  /**
   * 根据错误码得到对应的错误项（包含所有语言的错误描述信息）
   *
   * @param errorCode 错误码
   * @return Optional<ErrorItem>
   */
  public Optional<ErrorItem> getErrorItem(int errorCode) {
    return ErrorCodeI18n.getInstance().getErrorItem(errorCode);
  }

  @Inject
  public void setObjectMapper(ObjectMapper objectMapper) {
    I18nJsonUtil.getInstance(objectMapper);
  }

}

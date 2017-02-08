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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class I18nItem {

  private String lable;

  private Map<String, String> values;

  public I18nItem(String lable) {
    this.lable = lable;
    values = new HashMap<String, String>();
  }

  public I18nItem(String lable, Map<String, String> values) {
    this.lable = lable;
    this.values = values;
  }

  public String getLable() {
    return lable;
  }

  public Map<String, String> getValues() {
    return values;
  }

  public void addValue(String locale, String value) {
    values.put(locale, value);
  }

  public void unmodifiable() {
    values = Collections.unmodifiableMap(values);
  }
}

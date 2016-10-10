/**
 * Copyright 2016 ZTE Corporation.
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
package org.openo.carbon.bpel.config;

public class BpsExtraProperties extends AbstractBpsProperties {
  private static BpsExtraProperties instance = null;

  public static synchronized BpsExtraProperties getInstance() {
    if (instance == null) {
      instance = new BpsExtraProperties();
    }
    return instance;
  }

  @Override
  protected String getConfigName() {
    /*
     * String[] paths = null; URL url = BpsExtraProperties.class.getResource(""); if (url != null) {
     * paths = url.getPath().split("jar!"); } if (paths == null || paths.length <= 1) { return null;
     * }
     */
    return "wso2-config.properties";
  }
}

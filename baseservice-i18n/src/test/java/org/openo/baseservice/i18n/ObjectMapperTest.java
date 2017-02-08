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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class ObjectMapperTest {
  public static void main(String[] args) throws Exception {
    String s = "{" + "\"uca\":\"统一公共应用\"," + "\"Virtual Ne Name\":\"虚网元\"" + "}";
    ObjectMapper mapper = new ObjectMapper();
    HashMap m = mapper.readValue(s, HashMap.class);
    System.out.println(m.get("Virtual Ne Name"));
  }
}

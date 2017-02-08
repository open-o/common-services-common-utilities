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


import java.util.Optional;
import org.jvnet.hk2.annotations.Contract;
import org.openo.baseservice.i18n.ErrorCodeI18n.ErrorItem;

@Contract
public interface I18nService {

    /**
     * Get the corresponding examples of international documents (all languages), <br> for the above
     * example topology (for example, the Chinese definition: topo-i18n-zh-CN.json, English
     * definition Topo -i18n-en-US.json), into the reference into the "TOPO" can be <br> (i.e.
     * except "-i18n-*.json" after the exact match).
     *
     * @return Optional<I18n>
     */
    public Optional<I18n> getI18n(String i18nFilePrefix);

    public Optional<ErrorItem> getErrorItem(int errorCode);

}

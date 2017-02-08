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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class I18nJsonUtil {

    private static I18nJsonUtil util;
    private static Lock lock = new ReentrantLock();
    private ObjectMapper objectMapper;

    public I18nJsonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    <T extends Object> T readFromJson(InputStream ins, Class<T> clazz)
        throws JsonParseException, JsonMappingException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bs = new byte[1024 * 10];
        int length = -1;
        while ((length = ins.read(bs)) > 0) {
            baos.write(bs, 0, length);
        }
        bs = baos.toByteArray();

        byte[] ubs = null;
        if (bs.length > 3) {
            // 删除bom头 -17, -69, -65
            if (bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
                ubs = new byte[bs.length - 3];
                System.arraycopy(bs, 3, ubs, 0, ubs.length);
            }
        }
        if (ubs == null) {
            ubs = bs;
        }
        return objectMapper.readValue(ubs, clazz);
    }

    <T extends Object> T readFromJson(String str, Class<T> clazz)
        throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(str, clazz);
    }

    String writeToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    static I18nJsonUtil getInstance(ObjectMapper objectMapper) {
        if (util == null) {
            lock.lock();
            try {
                if (util == null) {
                    if (objectMapper == null) {
                        objectMapper = new ObjectMapper();
                    }
                    util = new I18nJsonUtil(objectMapper);
                }
            } finally {
                lock.unlock();
            }
        }
        return util;
    }

    static I18nJsonUtil getInstance() {
        return getInstance(null);
    }
}


/**
 * 国际化转换工具
 *
 * @author 10163976
 */
class I18nLocaleTransfer {

    private static Logger LOG = LoggerFactory.getLogger(I18nLocaleTransfer.class);

    /**
     * 方言转换<br> 如果存在直接返回<br> 如果不存在，则获取语言进行模糊匹配，未匹配上则返回默认方言<br>
     *
     * @param theLocale 待转换方言
     * @param locales 存在的方言
     * @return 转换后的方言
     */
    public static String transfer(Locale theLocale, Set<String> locales) {
        if (locales == null || locales.isEmpty()) {
            LOG.debug("locales is NULL or empty");
            return null;
        }
        if (theLocale == null) {
            String result = fetchDefault(locales);
            LOG.debug("transfer NULL --> " + result + " in " + locales);
            return result;
        }
        String locale = theLocale.toString();
        if (locale.isEmpty()) {
            String result = fetchDefault(locales);
            LOG.debug("transfer EMPTY --> " + result + " in " + locales);
            return result;
        }
        // 精确匹配
        if (locales.contains(locale)) {
            return locale;
        }

        // 根据语言模糊匹配
        String language = theLocale.getLanguage();
        if (locales.contains(language)) {
            LOG.debug("transfer " + locale + " --> " + language + " in " + locales);
            return language;
        }

        language = language + "_";
        for (String temp : locales) {
            if (temp.startsWith(language)) {
                LOG.debug("transfer " + locale + " --> " + temp + " in " + locales);
                return temp;
            }
        }
        String result = fetchDefault(locales);
        LOG.debug("transfer " + locale + " --> " + result + " in " + locales);
        return result;
    }

    /**
     * 返回默认方言，优先级为en,en_US,zh,zh_CN，如果都不存在，则随机返回一个
     */
    private static String fetchDefault(Set<String> locales) {
        if (locales.contains("en")) {
            return "en";
        } else if (locales.contains("en_US")) {
            return "en_US";
        }
        if (locales.contains("zh")) {
            return "zh";
        } else if (locales.contains("zh_CN")) {
            return "zh_CN";
        }
        return locales.iterator().next();
    }
}

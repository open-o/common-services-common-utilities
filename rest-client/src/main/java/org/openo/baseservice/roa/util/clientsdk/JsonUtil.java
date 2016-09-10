/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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
package org.openo.baseservice.roa.util.clientsdk;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import net.sf.json.JSON;

import java.io.IOException;

/**
 * JSON parse utilities.
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   28-May-2016
 */
public final class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setDeserializationConfig(MAPPER.getDeserializationConfig()
                .without(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
    }

    private JsonUtil() {
    }

    /**
     * Parse JSON formated string.<br/>
     * 
     * @param jsonstr: JSON formated string.
     * @param type: result type.
     * @return parsed object.
     * @throws IOException incase error.
     * @since  
     */
    public static <T> T unMarshal(final String jsonstr, final Class<T> type) throws IOException {
        return MAPPER.readValue(jsonstr, type);
    }

    /**
     * Parse JSON formatted string (for a generic target type).
     * <br/>
     * 
     * @param jsonstr request data.
     * @param type target type.
     * @return result object.
     * @throws IOException incase error.
     * @since  
     */
    public static <T> T unMarshal(final String jsonstr, final TypeReference<T> type) throws IOException {
        return MAPPER.readValue(jsonstr, type);
    }

    /**
     * Convert object to JSON format string.
     * <br/>
     * 
     * @param srcObj source object.
     * @return JSON format string.
     * @throws IOException incase of error.
     * @since  
     */
    public static String marshal(final Object srcObj) throws IOException {
        if(srcObj instanceof JSON) {
            return srcObj.toString();
        }
        return MAPPER.writeValueAsString(srcObj);
    }

    /**
     * Get parsing mapper
     * <br/>
     * 
     * @return parsing mapper
     * @since  
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}

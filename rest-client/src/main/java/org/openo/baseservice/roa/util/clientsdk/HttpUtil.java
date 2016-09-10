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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http utilities.
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   28-May-2016
 */
public class HttpUtil {

    /**
     * 
     */
    private static final String APPLICATION_JSON = "application/json";

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    private HttpUtil() {

    }

    /**
     * Check if the given array contains the given value (with case-insensitive comparison).
     * <br/>
     * 
     * @param array The array
     * @param value The value to search
     * @return true if the array contains the value
     * @since  
     */
    public static boolean containsIgnoreCase(final String[] array, final String value) {
        for(final String str : array) {
            if(value == null && str == null) {
                return true;
            }
            if(value != null && value.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Join an array of strings with the given separator.
     * <br/>
     * 
     * @param array The array of strings
     * @param separator The separator
     * @return the resulting string
     * @since  
     */
    public static String join(final String[] array, final String separator) {
        final int len = array.length;
        if(len == 0) {
            return "";
        }
        final StringBuilder out = new StringBuilder();
        out.append(array[0]);
        for(int i = 1; i < len; i++) {
            out.append(separator).append(array[i]);
        }
        return out.toString();
    }

    /**
     * Format the given parameter object into string.
     * <br/>
     * 
     * @param param param input
     * @return query param string
     * @since  
     */
    public static String parameterToString(final Object param) {
        if(param == null) {
            return "";
        } else if(param instanceof Date) {
            return Long.toString(((Date)param).getTime());
        } else if(param instanceof Collection) {
            final StringBuilder b = new StringBuilder();
            for(final Object o : (Collection)param) {
                if(b.length() > 0) {
                    b.append(',');
                }
                b.append(String.valueOf(o));
            }
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    /**
     * get accept header
     * <br/>
     * 
     * @param accepts accepts accept types
     * @return the accepts string
     * @since  
     */
    public static String selectHeaderAccept(final String[] accepts) {
        if(accepts.length == 0) {
            return null;
        }
        if(HttpUtil.containsIgnoreCase(accepts, APPLICATION_JSON)) {
            return APPLICATION_JSON;
        }
        return HttpUtil.join(accepts, ",");
    }

    /**
     * select head content type
     * <br/>
     * 
     * @param contentTypes contentTypes content types
     * @return the json string or the first content type
     * @since  
     */
    public static String selectHeaderContentType(final String[] contentTypes) {
        if(contentTypes.length == 0) {
            return APPLICATION_JSON;
        }
        if(HttpUtil.containsIgnoreCase(contentTypes, APPLICATION_JSON)) {
            return APPLICATION_JSON;
        }
        return contentTypes[0];
    }

    /**
     * Escape the given string to be used as URL query value.<br/>
     * 
     * @param str str param str
     * @return escape string
     * @since  
     */
    public static String escapeString(final String str) {
        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch(final UnsupportedEncodingException e) {
            LOGGER.info("UTF8 is not supported", e);
            return str;
        }
    }
}

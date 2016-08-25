/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.baseservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility functions for ROA.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 31-May-2016
 */
public final class RestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestUtils.class);

    private RestUtils() {

    }

    /**
     * To get body from http request<br/>
     * 
     * @param request : request object.
     * @return Request body as string.
     * @since SDNO 0.5
     */
    public static String getRequestBody(final HttpServletRequest request) {
        String body = null;
        final StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            final InputStream inputStream = request.getInputStream();
            if(inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                final char[] charBuffer = new char[128];
                int bytesRead = -1;
                while((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch(final IOException ex) {
            LOGGER.error("read inputStream buffer catch exception:", ex);
        } finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch(final IOException ex) {
                    LOGGER.error("close buffer catch exception:", ex);
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

}

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
package org.openo.baseservice.roa.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Service connection configuration util.<br/>
 * <p>
 * Get host and port from the Client Configure the connection environment and service Configure
 * profile
 * </p>
 * 
 * @author
 * @version   28-May-2016
 */
public class ServiceUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceUtil.class);

    private final Properties allConfigure = new Properties();

    private final Properties serviceConfigure;

    private String serviceStage;

    private String serviceName;

    /**
     * Constructor<br/>
     * <p>
     * Load profile information.
     * </p>
     * 
     * @since  
     * @param serviceName user-specified service name.
     * @param url invoked service url.
     */
    public ServiceUtil(final String serviceName, final String url) {
        final String fomattedUrl = formatUrl(url);
        serviceConfigure = loadProperties("service-configure.properties");
        if(null == serviceName || serviceName.isEmpty()) {
            this.serviceName = getServiceNameWhitUrl(fomattedUrl);
        } else {
            this.serviceName = serviceName;
        }
        loadPropertyFile();
    }

    /**
     * Get the service user-specified host.
     * <br/>
     * 
     * @return host
     * @since  
     */
    public String getServiceHost() {
        final String host = allConfigure.getProperty(serviceName + "." + serviceStage + ".host");
        if(null == host) {
            return "";
        }
        return host;
    }

    /**
     * Get the service user-specified port.
     * <br/>
     * 
     * @return port
     * @since  
     */
    public int getServicePort() {
        final String portStr = allConfigure.getProperty(serviceName + "." + serviceStage + ".port");
        if(null == portStr) {
            return -1;
        }
        return Integer.parseInt(portStr);
    }

    private String getServiceNameWhitUrl(final String url) {
        final Enumeration<?> keys = serviceConfigure.propertyNames();
        while(keys.hasMoreElements()) {
            final String key = (String)keys.nextElement();
            if(key.endsWith("urls")) {
                final String urls = serviceConfigure.getProperty(key);
                for(String tempUrl : urls.split(",")) {
                    tempUrl = formatUrl(tempUrl);
                    if(url.startsWith(tempUrl)) {
                        return key.split("\\.")[0];
                    }
                }

            }
        }
        return "";
    }

    private static String formatUrl(final String url) {
        String outUrl = url;
        if(outUrl.contains("?")) {
            outUrl = outUrl.split("\\?")[0];
        }
        outUrl = outUrl.replace("\\", "/");
        outUrl = outUrl.replaceAll("[/]{2,}", "/");
        outUrl = outUrl.endsWith("/") ? outUrl.substring(0, outUrl.length() - 1) : outUrl;
        outUrl = outUrl.endsWith("/*") ? outUrl.substring(0, outUrl.length() - 2) : outUrl;
        return outUrl;
    }

    /**
     * Loads the client and service configuration files.
     * <br/>
     * 
     * @since  
     */
    private void loadPropertyFile() {
        final Properties clientConfigure = loadProperties(serviceName + "-client-configure.properties");
        allConfigure.putAll(clientConfigure);
        allConfigure.putAll(serviceConfigure);
        serviceStage = allConfigure.getProperty(serviceName + ".stage");
    }

    /**
     * Loads the client and service configuration files.
     * <br/>
     * 
     * @param classProperties: service profile file name.
     * @return Service configuration.
     * @since  
     */
    private Properties loadProperties(final String classProperties) {
        final Properties properties = new Properties();
        InputStream inputStream = null;
        ClassLoader classloader = null;
        try {
            classloader = this.getClass().getClassLoader();
            if(classloader != null) {
                inputStream = classloader.getResourceAsStream(classProperties);
            }
            if(inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            }
        } catch(final IOException e) {
            LOG.error("load file error: ", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch(final IOException ee) {
                    LOG.error("close inputStream  error: ", ee);
                }
                inputStream = null;
            }
        }
        return properties;
    }
}

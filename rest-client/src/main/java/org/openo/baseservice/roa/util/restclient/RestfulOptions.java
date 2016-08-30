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
package org.openo.baseservice.roa.util.restclient;

import java.util.HashMap;
import java.util.Map;

/**
 * Options for Rest communication.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 28-May-2016
 */
public class RestfulOptions {

    public static final String REST_OPTIONS_NAME_TIMEOUT = "timeout";

    public static final int REST_OPTIONS_TIMEOUT_MAXTIMEOUT = 1800000;

    private final Map<String, Object> optionsMap = new HashMap<>();

    /**
     * Set called service name.<br/>
     * 
     * @param serviceName service name.
     * @return true.
     * @since SDNO 0.5
     */
    public boolean setCalledServiceName(final String serviceName) {
        this.setOption(RestfulClientConst.CALLED_SERVICE_NAME, serviceName);
        return true;
    }

    /**
     * Get called service name.<br/>
     * 
     * @return
     * @since SDNO 0.5
     */
    public String getCalledServicName() {
        final Object obj = this.getOption(RestfulClientConst.CALLED_SERVICE_NAME);
        if(null == obj) {
            return "";
        }
        return (String)obj;
    }

    /**
     * Get port.<br/>
     * 
     * @return port.
     * @since SDNO 0.5
     */
    public int getPort() {
        final Object obj = this.getOption(RestfulClientConst.PORT_KEY_NAME);
        if(null == obj) {
            return 0;
        }
        return ((Integer)obj).intValue();
    }

    /**
     * Set port.<br/>
     * 
     * @param port port to set.
     * @return
     * @since SDNO 0.5
     */
    public boolean setPort(final int port) {
        this.setOption(RestfulClientConst.PORT_KEY_NAME, port);
        return true;
    }

    /**
     * Get host.<br/>
     * 
     * @return the host.
     * @since SDNO 0.5
     */
    public String getHost() {
        final Object obj = this.getOption(RestfulClientConst.HOST_KEY_NAME);
        if(null == obj) {
            return "";
        }
        return (String)obj;
    }

    /**
     * Set host.<br/>
     * 
     * @param host host to set.
     * @return
     * @since SDNO 0.5
     */
    public boolean setHost(final String host) {
        this.setOption(RestfulClientConst.HOST_KEY_NAME, host);
        return true;
    }

    /**
     * Set rest time-out.<br/>
     * 
     * @param timeout time-out to set in seconds.
     * @return
     * @since SDNO 0.5
     */
    public boolean setRestTimeout(final int timeout) {
        if(0 < timeout && REST_OPTIONS_TIMEOUT_MAXTIMEOUT >= timeout) {
            this.setOption(REST_OPTIONS_NAME_TIMEOUT, timeout);
            return true;
        }
        return false;
    }

    /**
     * Get time-out.<br/>
     * 
     * @return time-out in seconds.
     * @since SDNO 0.5
     */
    public int getRestTimeout() {
        final Object obj = this.getOption(REST_OPTIONS_NAME_TIMEOUT);
        if(null == obj) {
            return 0;
        }
        return ((Integer)obj).intValue();
    }

    /**
     * Get specified option.<br/>
     * 
     * @param optionName option name.
     * @return option
     * @since SDNO 0.5
     */
    public Object getOption(final String optionName) {
        return optionsMap.get(optionName);
    }

    /**
     * Get option value as integer.<br/>
     * 
     * @param optionName option name.
     * @return option value as int.
     * @since SDNO 0.5
     */
    public int getIntOption(final String optionName) {
        final Object obj = this.getOption(optionName);
        if(null == obj) {
            return 0;
        }
        return ((Integer)obj).intValue();
    }

    /**
     * Get option value as string.<br/>
     * 
     * @param optionName option name.
     * @return option value as string.
     * @since SDNO 0.5
     */
    public String getStringOption(final String optionName) {
        final Object obj = this.getOption(optionName);
        if(null == obj) {
            return "";
        }
        return (String)obj;
    }

    /**
     * Set option.<br/>
     * 
     * @param option option name.
     * @param optionsValue option value.
     * @return
     * @since SDNO 0.5
     */
    public Object setOption(final String option, final Object optionsValue) {
        return optionsMap.put(option, optionsValue);
    }
}
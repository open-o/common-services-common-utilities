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

package org.openo.baseservice.bus.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.openo.baseservice.util.impl.SystemEnvVariablesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide the service register cbb for common use. <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version
 */
public class RegisterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);

    private static String busPath = null;

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @throws IOException
     * 
     * @since
     */
    private RegisterService() {
    }

    /**
     * register the micro service. <br/>
     * 
     * @param jsonPath:
     *            the service json object to register to the bus.
     * @param createOrUpdate:
     *            true, create and update the old ip port. false, create and
     *            delete the old one;
     * @return
     * @throws IOException
     * @since
     */
    public static Response registerService(String jsonPath, boolean createOrUpdate) throws IOException {

        String serviceInfo = getServiceModel(jsonPath);

        WebClient client = initializeClient();

        client.type(BusConstant.APPLICATION_JSON_HEADER);

        client.accept(BusConstant.APPLICATION_JSON_HEADER);

        client.path(BusConstant.BUS_SERVICE_URL);

        client.query(BusConstant.CREATE_OR_UPDATE, createOrUpdate);

        LOGGER.info("Connecting bus address : " + busPath + BusConstant.BUS_SERVICE_URL);

        return client.invoke(BusConstant.POST_METHOD, serviceInfo);

    }

    /**
     * get the service's model. and return it as a string ; <br/>
     * 
     * @param jsonPath
     * @return
     * @since
     */
    private static String getServiceModel(String jsonPath) {

        String serviceInfo = "";

        try {
            LOGGER.info("begin to read file micro service json " + jsonPath);

            FileInputStream busFile = new FileInputStream(jsonPath);

            int size = busFile.available();

            byte[] buffer = new byte[size];

            busFile.read(buffer);

            busFile.close();

            serviceInfo = new String(buffer);
            LOGGER.info("finished to read micro service json file. ");
        } catch (Exception ex) {
            LOGGER.error("Read the micro service json file error :", ex);
        }
        return serviceInfo;
    }

    /**
     * initialize the bus ip and port. <br/>
     * 
     * @return
     * @throws IOException
     * @since
     */
    private static String getBusAddress() throws IOException {

        LOGGER.info("begin to get the bus baseurl.");
        FileInputStream busFile = null;
        String url = BusConstant.MICROSERVICE_DEFAULT;

        String filePath = SystemEnvVariablesFactory.getInstance().getAppRoot() + BusConstant.BUS_CONFIGURE_FILE;
        LOGGER.info("bus base url file:" + filePath);

        Properties properties = new Properties();

        try {
            busFile = new FileInputStream(filePath);
            properties.load(busFile);
            url = properties.getProperty(BusConstant.BUS_ADDRESS_KEY);
        } catch (IOException e) {
            if (busFile != null) {
                busFile.close();
            }
            LOGGER.error("Read the bus url failed: ", e);
        }

        LOGGER.info("initialize the bus baseurl is: " + url);
        return BusConstant.HTTP_HEAD + url;
    }

    /**
     * get the bus's client's address. and initialize the web client. <br/>
     * 
     * @return
     * @throws IOException
     * @since
     */
    private static WebClient initializeClient() throws IOException {

        final List<Object> providers = new ArrayList<Object>();

        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();

        providers.add(jacksonJsonProvider);

        if (busPath == null) {
            busPath = getBusAddress();
        }

        return WebClient.create(busPath, providers);
    }
}

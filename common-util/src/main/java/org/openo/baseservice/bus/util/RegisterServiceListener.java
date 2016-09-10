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

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openo.baseservice.util.impl.SystemEnvVariablesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initialize the service register listener.
 * <br/>
 * <p>  
 * </p>
 * 
 * @author
 * @version  
 */
public class RegisterServiceListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceListener.class);
    
    private static final String JSON = "json";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        String servicePath = SystemEnvVariablesFactory.getInstance().getAppRoot() + "/etc/microservice";
        LOGGER.info("microservices json file path is" + servicePath);

        File file = new File(servicePath);

        File[] fileList = file.listFiles();

        for(File tempFile : fileList) {
            String fileName = tempFile.getName();
            if (fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase(JSON)) {
                LOGGER.info("begin to initialize the service file" + tempFile.getAbsolutePath());
                
                /** now because ZTE do not provide the service bus.commont this code first.
                try {
                    RegisterService.registerService(tempFile.getAbsolutePath(), true);
                } catch(IOException e) {
                    LOGGER.error("Faile to register the service file :" + tempFile.getPath() + ", exception:" + e);
                }
                */
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }


}

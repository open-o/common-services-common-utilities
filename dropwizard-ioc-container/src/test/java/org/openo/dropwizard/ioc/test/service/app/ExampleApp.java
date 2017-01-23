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
package org.openo.dropwizard.ioc.test.service.app;

import org.openo.dropwizard.ioc.bundle.IOCApplication;
import org.openo.dropwizard.ioc.test.service.conf.TestConfiguration;

public class ExampleApp extends IOCApplication<TestConfiguration>{

    private static final String CONFIGURATION_FILE = "example.yml";

    public static void main(String[] args) throws Exception {
    	
    	String configFile = ExampleApp.class.getClassLoader().getResource(CONFIGURATION_FILE).getFile();
    	
    	args = new String[]{"server",configFile};
    	
    	new ExampleApp().run(args);
    }


}

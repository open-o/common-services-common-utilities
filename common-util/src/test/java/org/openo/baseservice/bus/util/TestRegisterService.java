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
import java.io.FileInputStream;
import java.io.IOException;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.util.impl.SystemEnvVariablesDefImpl;

import junit.framework.Assert;
import mockit.Mock;
import mockit.MockUp;

public class TestRegisterService {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    public void testregisterServce() throws IOException {
        
        File file = new File("");
        Response res = null;
        
        final String path = file.getAbsolutePath();
        
        new MockUp<SystemEnvVariablesDefImpl>() {
            @Mock
            public String getAppRoot() {
                return path;
            }
        };
        
        try {
            res = RegisterService.registerService(path, true);
        } catch(Exception e) {
            Assert.assertNotNull(e);
        }
        
    }
}


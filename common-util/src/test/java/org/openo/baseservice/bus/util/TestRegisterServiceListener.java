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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

public class TestRegisterServiceListener {
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
    @Test
    public void testRegisterServiceListener() {
        
        RegisterServiceListener impl = new RegisterServiceListener();
        
        ServletContextEvent sce = null;
        
        new MockUp<File>() {
            @Mock
            public File[] listFiles() {
                File file = new File("");
                File[] filelist = new File[]{file};
                return filelist;
            }
            
        };
        
        impl.contextInitialized(sce);
    }
    
}

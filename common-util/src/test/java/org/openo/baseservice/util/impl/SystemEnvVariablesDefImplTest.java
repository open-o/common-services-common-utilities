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
package org.openo.baseservice.util.impl;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openo.baseservice.util.inf.SystemEnvVariables;

import junit.framework.Assert;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import net.jcip.annotations.NotThreadSafe;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   08-Jun-2016
 */
@RunWith(JMockit.class)
@NotThreadSafe
public class SystemEnvVariablesDefImplTest {

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since  
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since  
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since  
     */
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetAppRootException() throws Exception {
        new NonStrictExpectations() {

            @Mocked
            File file;

            {
                file = new File(".");
                file.getCanonicalPath();
                result = new IOException();
            }

        };
        final SystemEnvVariables envVars =new SystemEnvVariablesDefImpl();
        System.setProperty("catalina.base", ".");
        final String actual = envVars.getAppRoot();
        Assert.assertEquals(null, actual);
    }


    /**
     * Test method for
     * {@link org.openo.baseservice.util.impl.SystemEnvVariablesDefImpl#getAppRoot()}.
     * 
     * @throws Exception
     */
    @Test
    public void testGetAppRoot() throws Exception {
        final SystemEnvVariables envVars = new SystemEnvVariablesDefImpl();
        final File file = new File(".");
        final String expected = file.getCanonicalPath();
        System.setProperty("catalina.base", ".");
        final String actual = envVars.getAppRoot();
        Assert.assertEquals(expected, actual);
    }

}

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

package org.openo.baseservice.roa.util.restclient;

import static org.junit.Assert.assertEquals;

import org.openo.baseservice.util.impl.SystemEnvVariablesDefImpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import net.sf.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import mockit.Mock;
import mockit.MockUp;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   20-Jun-2016
 */
public class TestRestfulConfigure {

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since  
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        setAppRoot();
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

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since  
     */
    @After
    public void tearDown() throws Exception {
    }

    private static void setAppRoot() {
        final URL resource = ClassLoader.getSystemResource("etc/conf/restclient.json");
        final String urlPath = resource.getPath().replace("etc/conf/restclient.json", "");

        try {
            final String path = new File(urlPath).getCanonicalPath();
            System.out.println("path: " + path);

            System.setProperty("catalina.base", path);
            System.out.println("approot:" + System.getProperty("catalina.base"));
        } catch(final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Ignore
    @Test
    public final void testRestfulConfigure() throws Exception {
        final RestfulConfigure configure = new RestfulConfigure();
        final RestfulOptions options = configure.getOptions();
        assertEquals("127.0.0.1", options.getHost());
        assertEquals(8080, options.getPort());
        assertEquals(1000, options.getIntOption("ConnectTimeout"));
        assertEquals(10, options.getIntOption("thread"));
        assertEquals(500000, options.getIntOption("idletimeout"));
        assertEquals(10000, options.getIntOption("timeout"));

    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Ignore
    @Test
    public final void testRestfulConfigureAppRootNull() throws Exception {
        new MockUp<SystemEnvVariablesDefImpl>() {

            @Mock
            public String getAppRoot() {
                return null;
            }

        };
        final RestfulConfigure configure = new RestfulConfigure();
        final RestfulOptions options = configure.getOptions();

        assertEquals("", options.getHost());
        assertEquals(0, options.getPort());
        assertEquals(3000, options.getIntOption("ConnectTimeout"));
        assertEquals(200, options.getIntOption("thread"));
        assertEquals(30000, options.getIntOption("idletimeout"));
        assertEquals(30000, options.getIntOption("timeout"));

    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Ignore
    @Test
    public final void testRestfulConfigureDefault() throws Exception {

        {

            new MockUp<File>() {

                @Mock
                public boolean isFile() {
                    return false;
                }
            };
            final RestfulConfigure configure = new RestfulConfigure();
            final RestfulOptions options = configure.getOptions();
            assertEquals("", options.getHost());
            assertEquals(0, options.getPort());
            assertEquals(3000, options.getIntOption("ConnectTimeout"));
            assertEquals(200, options.getIntOption("thread"));
            assertEquals(30000, options.getIntOption("idletimeout"));
            assertEquals(30000, options.getIntOption("timeout"));
        }

    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Ignore
    @Test
    public final void testRestfulConfigureException() throws Exception {

        new MockUp<JSONObject>() {

            @Mock
            JSONObject fromObject(final Object object) throws IOException {
                throw new IOException();
            }
        };

        final RestfulConfigure configure = new RestfulConfigure();
        final RestfulOptions options = configure.getOptions();
        assertEquals("", options.getHost());
        assertEquals(0, options.getPort());
        assertEquals(3000, options.getIntOption("ConnectTimeout"));
        assertEquals(200, options.getIntOption("thread"));
        assertEquals(30000, options.getIntOption("idletimeout"));
        assertEquals(30000, options.getIntOption("timeout"));

    }

    /**
     * <br/>
     * 
     * @since  
     */
    @Ignore
    @Test
    public final void testRestfulConfigureString() {
        final String configFile = "rest-client-test.json";
        final String appRoot = System.getProperty("catalina.base");
        final RestfulConfigure configure = new RestfulConfigure(appRoot + File.separator + configFile);
        final RestfulOptions options = configure.getOptions();
        assertEquals("10.10.10.10", options.getHost());
        assertEquals(443, options.getPort());
        assertEquals(10, options.getIntOption("ConnectTimeout"));
        assertEquals(100, options.getIntOption("thread"));
        assertEquals(30, options.getIntOption("idletimeout"));
        assertEquals(60, options.getIntOption("timeout"));
    }

    /**
     * <br/>
     * 
     * @since  
     */
    @Ignore
    @Test
    public final void testGetOptions() {
    }
}
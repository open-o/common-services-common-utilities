/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
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
package org.openo.baseservice.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 08-Jun-2016
 */
@RunWith(JMockit.class)
public class RestUtilsTest {

    @Mocked
    HttpServletRequest mockHttpServletRequest;

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since SDNO 0.5
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since SDNO 0.5
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since SDNO 0.5
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since SDNO 0.5
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.openo.baseservice.util.RestUtils#getRequestBody(javax.servlet.http.HttpServletRequest)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testGetRequestBody() throws IOException {
        final String dummy = "this is a dummy data to test request body";
        final ServletInputStream inputStream = new ServletInputStream() {

            final ByteArrayInputStream stream = new ByteArrayInputStream(dummy.getBytes());

            @Override
            public int read() throws IOException {
                return stream.read();
            }

        };

        new Expectations() {

            {
                mockHttpServletRequest.getInputStream();
                returns(inputStream);
            }
        };
        final String body = RestUtils.getRequestBody(mockHttpServletRequest);

        Assert.assertEquals(dummy, body);
    }

    @Test
    public void testGetRequestBodyNull() throws IOException {
        final ServletInputStream inputStream = null;
        new Expectations() {

            {
                mockHttpServletRequest.getInputStream();
                returns(inputStream);
            }
        };
        final String body = RestUtils.getRequestBody(mockHttpServletRequest);

        Assert.assertEquals("", body);
    }

    @Test
    public void testGetRequestBodyIOException() throws IOException {
        final ServletInputStream inputStream = new ServletInputStream() {

            @Override
            public int read() throws IOException {
                throw new IOException();
            }

        };

        new Expectations() {

            {
                mockHttpServletRequest.getInputStream();
                returns(inputStream);
            }
        };
        final String body = RestUtils.getRequestBody(mockHttpServletRequest);

        Assert.assertEquals("", body);
    }

    @Test
    public void testGetRequestBodyCloseIOException() throws IOException {
        final ServletInputStream inputStream = new ServletInputStream() {

            final ByteArrayInputStream stream = new ByteArrayInputStream("dummy".getBytes());

            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };

        new Expectations() {

            {
                mockHttpServletRequest.getInputStream();
                returns(inputStream);
            }
        };
        final String body = RestUtils.getRequestBody(mockHttpServletRequest);

        Assert.assertEquals("dummy", body);
    }

}

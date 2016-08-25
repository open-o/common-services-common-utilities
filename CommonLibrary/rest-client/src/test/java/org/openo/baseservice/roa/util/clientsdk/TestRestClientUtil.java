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

package org.openo.baseservice.roa.util.clientsdk;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulAsyncCallback;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Date;

import junit.framework.Assert;
import mockit.Expectations;
import mockit.Mocked;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 13-Jun-2016
 */
public class TestRestClientUtil {

    @Mocked
    Restful restFullMock;

    ExpectedException thrown = ExpectedException.none();;

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
     * <br/>
     * 
     * @throws ServiceException
     * @since SDNO 0.5
     */
    @Ignore
    @Test
    public void testInvokeMethod() throws ServiceException {
        final String path = "test/path";
        final RestfulParametes parameters = null;
        final RestfulResponse expected = new RestfulResponse();
        expected.setStatus(200);

        new Expectations() {

            {
                restFullMock.get(path, parameters);
                returns(expected);

                restFullMock.post(path, parameters);
                returns(expected);

                restFullMock.patch(path, parameters);
                returns(expected);

                restFullMock.delete(path, parameters);
                returns(expected);

                restFullMock.put(path, parameters);
                returns(expected);
            }
        };
        RestfulResponse actual = RestClientUtil.invokeMethod("GET", path, parameters, restFullMock);
        Assert.assertEquals(200, actual.getStatus());

        actual = RestClientUtil.invokeMethod("POST", path, parameters, restFullMock);
        Assert.assertEquals(200, actual.getStatus());

        actual = RestClientUtil.invokeMethod("PATCH", path, parameters, restFullMock);
        Assert.assertEquals(200, actual.getStatus());

        actual = RestClientUtil.invokeMethod("DELETE", path, parameters, restFullMock);
        Assert.assertEquals(200, actual.getStatus());

        actual = RestClientUtil.invokeMethod("PUT", path, parameters, restFullMock);
        Assert.assertEquals(200, actual.getStatus());
    }

    @Ignore
    @Test(expected = ServiceException.class)
    public void testInvokeMethodException() throws ServiceException {
        RestClientUtil.invokeMethod("UNKNOWN-METHOD", "some/path", null, restFullMock);
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since SDNO 0.5
     */
    @Ignore
    @Test(expected = ServiceException.class)
    public void testInvokeAsyncMethod() throws ServiceException {
        final String path = "test/path";
        final RestfulParametes parameters = null;
        final RestfulAsyncCallback callback = null;

        RestClientUtil.invokeAsyncMethod("GET", path, parameters, restFullMock, callback);

        RestClientUtil.invokeAsyncMethod("POST", path, parameters, restFullMock, callback);

        RestClientUtil.invokeAsyncMethod("PATCH", path, parameters, restFullMock, callback);

        RestClientUtil.invokeAsyncMethod("DELETE", path, parameters, restFullMock, callback);

        RestClientUtil.invokeAsyncMethod("PUT", path, parameters, restFullMock, callback);

        RestClientUtil.invokeAsyncMethod("UNKNOWN", path, parameters, restFullMock, callback);
    }

    /**
     * <br/>
     * 
     * @since SDNO 0.5
     */
    @Ignore
    @Test
    public void testIsPrimitive() {

        Assert.assertTrue(RestClientUtil.isPrimitive(Integer.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(Long.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(Double.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(Void.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(String.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(Boolean.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(Byte.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(Character.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(Short.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(Float.class));

        Assert.assertTrue(RestClientUtil.isPrimitive(int.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(long.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(double.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(void.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(boolean.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(byte.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(char.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(short.class));
        Assert.assertTrue(RestClientUtil.isPrimitive(float.class));

        Assert.assertFalse(RestClientUtil.isPrimitive(Object.class));
        Assert.assertFalse(RestClientUtil.isPrimitive(Date.class));
        Assert.assertFalse(RestClientUtil.isPrimitive(Arrays.class));
    }
}

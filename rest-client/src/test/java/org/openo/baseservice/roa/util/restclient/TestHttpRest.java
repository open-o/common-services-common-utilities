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
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openo.baseservice.remoteservice.exception.ServiceException;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   13-Jun-2016
 */
@RunWith(JMockit.class)
public class TestHttpRest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since  
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testInitHttpRest() throws Exception {
        final RestfulOptions options = new RestfulOptions();
        new MockUp<HttpClient>() {

            @Mock
            public void doStart() {
                System.out.println("started");
            }
        };
        final HttpRest httpRest = new HttpRest();
        httpRest.initHttpRest(options);
        final Field httpClient = HttpBaseRest.class.getDeclaredField("client");
        httpClient.setAccessible(true);
        Assert.assertNotNull(httpClient.get(httpRest));
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testInitHttpRestExcpetion() throws Exception {
        final RestfulOptions options = new RestfulOptions();
        new MockUp<HttpClient>() {

            @Mock
            public void doStart() throws Exception {
                throw new Exception();
            }
        };
        final HttpRest httpRest = new HttpRest();
        thrown.expect(ServiceException.class);
        thrown.expectMessage("http client init failed.");
        httpRest.initHttpRest(options);
        final Field httpClient = HttpRest.class.getDeclaredField("client");
        httpClient.setAccessible(true);
        Assert.assertNull(httpClient.get(httpRest));
        System.out.println("finished");
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testInitHttpRestNull() throws Exception {
        final HttpRest httpRest = new HttpRest();
        thrown.expect(ServiceException.class);
        thrown.expectMessage("option is null.");
        httpRest.initHttpRest(null);
    }

    /**
     * <br/>
     * 
     * @throws NoSuchFieldException
     * @throws Exception
     * @since  
     */
    @Test
    public void testCreateRestHttpContentExchange() throws NoSuchFieldException, Exception {
        final HttpBaseRest httpRest = new HttpRest();
        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {

            }

            @Override
            public void handleExcepion(final Throwable e) {

            }

        };
        final RestHttpContentExchange exchange = httpRest.createRestHttpContentExchange(callback);
        assertNotNull(exchange);
        final Field callbackField = RestHttpContentExchange.class.getDeclaredField("callback");
        assertNotNull(callbackField);
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testGetStringRestfulParametes() throws Exception {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parametes = new RestfulParametes();
        parametes.put("id", "1234");
        parametes.put("name", "some-name");
        parametes.put("address", null);
        parametes.putHttpContextHeader("Content-Type", "application/json");
        parametes.putHttpContextHeader("Accept-Encoding", "*/*");
        final RestfulResponse response = httpRest.get("path/to/service", parametes);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());

    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testGetStringRestfulParametesRestfulOptions() throws Exception {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulResponse response = httpRest.get("path/to/service", new RestfulParametes(), options);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testGetStringRestfulParametesEncodeError() throws Exception {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        new NonStrictExpectations() {

            @Mocked
            URLEncoder encoder;

            {
                URLEncoder.encode(anyString, RestfulClientConst.ENCODING);
                result = new UnsupportedEncodingException();
            }

        };

        thrown.expect(ServiceException.class);
        thrown.expectMessage("Broken VM does not support");

        final RestfulParametes parametes = new RestfulParametes();
        parametes.put("id", "1234");
        parametes.put("name", "some-name");
        parametes.put("address", null);
        parametes.putHttpContextHeader("Content-Type", "application/json");
        parametes.putHttpContextHeader("Accept-Encoding", "*/*");
        final RestfulResponse response = httpRest.get("path/to/service", parametes);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());

    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testHeadStringRestfulParametes() throws Exception {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parametes = new RestfulParametes();
        parametes.put("id", "1234");
        parametes.put("name", "some-name");
        parametes.put("address", null);
        parametes.putHttpContextHeader("Content-Type", "");
        parametes.putHttpContextHeader("Accept-Encoding", "");
        final RestfulResponse response = httpRest.head("path/to/service", parametes);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testHeadStringRestfulParametesRestfulOptions() throws Exception {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parametes = new RestfulParametes();
        parametes.put("id", "1234");
        parametes.put("name", "some-name");
        parametes.put("address", null);
        parametes.putHttpContextHeader("Content-Type", "");
        parametes.putHttpContextHeader("Accept-Encoding", "");
        final RestfulResponse response = httpRest.head("path/to/service", parametes, options);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @param options
     * @return
     * @throws ServiceException
     * @since  
     */
    private HttpRest getHttpRest(final RestfulOptions options) throws ServiceException {
        final HttpRest httpRest = new HttpRest();
        {
            new MockUp<HttpClient>() {

                @Mock
                public void doStart() {
                    System.out.println("started");
                }

                @Mock
                public void send(final HttpExchange exchange) throws IOException {
                    System.out.println("send");
                }
            };
            httpRest.initHttpRest(options);

        }
        return httpRest;
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testAsyncGetStringRestfulParametesRestfulAsyncCallback() throws Exception {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncGet("path/to/service", new RestfulParametes(), callback);
        httpRest.asyncGet("path/to/service", new RestfulParametes(), null);
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testAsyncGetStringRestfulParametesRestfulOptionsRestfulAsyncCallback() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncGet("path/to/service", new RestfulParametes(), new RestfulOptions(), callback);
        httpRest.asyncGet("path/to/service", new RestfulParametes(), new RestfulOptions(), null);
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testPutStringRestfulParametes() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parametes = new RestfulParametes();
        parametes.put("id", "1234");
        parametes.put("name", "some-name");
        parametes.put("address", null);
        parametes.putHttpContextHeader("Content-Type", "");
        parametes.putHttpContextHeader("Accept-Encoding", "");
        final RestfulResponse response = httpRest.put("path/to/service", parametes);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testPutStringRestfulParametesRestfulOptions() throws ServiceException {

        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parametes = new RestfulParametes();
        parametes.put("id", "1234");
        parametes.put("name", "some-name");
        parametes.put("address", null);
        parametes.putHttpContextHeader("Content-Type", "");
        parametes.putHttpContextHeader("Accept-Encoding", "");
        final RestfulResponse response = httpRest.put("path/to/service", parametes, null);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testAsyncPutStringRestfulParametesRestfulAsyncCallback() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncPut("path/to/service", new RestfulParametes(), callback);
        httpRest.asyncPut("path/to/service", new RestfulParametes(), null);
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testAsyncPutStringRestfulParametesRestfulOptionsRestfulAsyncCallback() throws Exception {
        final RestfulOptions options = new RestfulOptions();

        final HttpRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncPut("path/to/service", new RestfulParametes(), new RestfulOptions(), callback);
        httpRest.asyncPut("path/to/service", new RestfulParametes(), new RestfulOptions(), null);
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testPostStringRestfulParametes() throws Exception {
        final RestfulOptions options = new RestfulOptions();

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone");
                return HttpExchange.STATUS_EXPIRED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_EXPIRED);
                return response;
            }

        };
        final RestfulParametes parameters = new RestfulParametes();
        parameters.put("id", "1234");
        parameters.put("name", "some-name");
        parameters.put("address", null);
        parameters.putHttpContextHeader("Content-Type", "");
        parameters.putHttpContextHeader("Accept-Encoding", "");

        thrown.expect(ServiceException.class);
        thrown.expectMessage("request is expierd");
        final RestfulResponse response = httpRest.post("http://localhost:80/path/to/service", parameters);
        assertEquals(HttpExchange.STATUS_EXPIRED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testPostStringRestfulParametesRestfulOptions() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone" + HttpExchange.STATUS_EXCEPTED);
                return HttpExchange.STATUS_EXCEPTED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parameters = new RestfulParametes();
        parameters.put("id", "1234");
        parameters.put("name", "some-name");
        parameters.put("address", null);
        parameters.setRawData("{ \"data\"=\"sample JSON data\"");
        parameters.putHttpContextHeader("Content-Type", "");
        parameters.putHttpContextHeader("Accept-Encoding", "");
        thrown.expect(ServiceException.class);
        thrown.expectMessage("request is exception");
        final RestfulResponse response = httpRest.post("path/to/service", parameters, null);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testAsyncPostStringRestfulParametesRestfulAsyncCallback() throws Exception {
        final RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(10);

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone:" + HttpExchange.STATUS_EXCEPTED);
                return 99;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_EXCEPTED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncPost("path/to/service", new RestfulParametes(), options, callback);
        httpRest.asyncPost("path/to/service", new RestfulParametes(), options, null);
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testAsyncPostStringRestfulParametesRestfulOptionsRestfulAsyncCallback() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(10);

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone:" + HttpExchange.STATUS_COMPLETED);
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncPost("path/to/service", new RestfulParametes(), options, callback);
        httpRest.asyncPost("path/to/service", new RestfulParametes(), options, null);
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testDeleteStringRestfulParametes() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();

        final HttpBaseRest httpRest = getHttpRest(options);

        final RestfulResponse response = httpRest.delete("path/to/service", null);
        assertEquals(-1, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testDeleteStringRestfulParametesRestfulOptions() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone" + HttpExchange.STATUS_COMPLETED);
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parameters = new RestfulParametes();
        parameters.put("id", "1234");
        parameters.put("name", "some-name");
        parameters.put("address", null);
        parameters.setRawData("{ \"data\"=\"sample JSON data\"");
        parameters.putHttpContextHeader("Content-Type", "");
        parameters.putHttpContextHeader("Accept-Encoding", "");
        final RestfulResponse response = httpRest.delete("path/to/service", parameters, options);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testAsyncDeleteStringRestfulParametesRestfulAsyncCallback() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(10);

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone:" + HttpExchange.STATUS_COMPLETED);
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncDelete("path/to/service", new RestfulParametes(), callback);
        httpRest.asyncDelete("path/to/service", new RestfulParametes(), null);
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testAsyncDeleteStringRestfulParametesRestfulOptionsRestfulAsyncCallback() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(10);

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone:" + HttpExchange.STATUS_COMPLETED);
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncDelete("path/to/service", new RestfulParametes(), options, callback);
        httpRest.asyncDelete("path/to/service", new RestfulParametes(), options, null);
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testPatchStringRestfulParametes() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone" + HttpExchange.STATUS_COMPLETED);
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parameters = new RestfulParametes();
        parameters.put("id", "1234");
        parameters.put("name", "some-name");
        parameters.put("address", null);
        parameters.setRawData("{ \"data\"=\"sample JSON data\"");
        parameters.putHttpContextHeader("Content-Type", "");
        parameters.putHttpContextHeader("Accept-Encoding", "");
        final RestfulResponse response = httpRest.patch("path/to/service", parameters);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testPatchStringRestfulParametesRestfulOptions() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone" + HttpExchange.STATUS_COMPLETED);
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };
        final RestfulParametes parameters = new RestfulParametes();
        parameters.put("id", "1234");
        parameters.put("name", "some-name");
        parameters.put("address", null);
        parameters.setRawData("{ \"data\"=\"sample JSON data\"");
        parameters.putHttpContextHeader("Content-Type", "");
        parameters.putHttpContextHeader("Accept-Encoding", "");
        final RestfulResponse response = httpRest.patch("path/to/service", parameters, options);
        assertEquals(HttpExchange.STATUS_COMPLETED, response.getStatus());
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testAsyncPatchStringRestfulParametesRestfulAsyncCallback() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(10);

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone:" + HttpExchange.STATUS_COMPLETED);
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncPatch("path/to/service", new RestfulParametes(), callback);
        httpRest.asyncPatch("path/to/service", new RestfulParametes(), null);
    }

    /**
     * <br/>
     * 
     * @throws ServiceException
     * @since  
     */
    @Test
    public void testAsyncPatchStringRestfulParametesRestfulOptionsRestfulAsyncCallback() throws ServiceException {
        final RestfulOptions options = new RestfulOptions();
        options.setRestTimeout(10);

        final HttpBaseRest httpRest = getHttpRest(options);
        new MockUp<RestHttpContentExchange>() {

            @Mock
            public int waitForDone() {
                System.out.println("waitForDone:" + HttpExchange.STATUS_COMPLETED);
                return HttpExchange.STATUS_COMPLETED;
            }

            @Mock
            public RestfulResponse getResponse() throws IOException {
                final RestfulResponse response = new RestfulResponse();
                response.setStatus(HttpExchange.STATUS_COMPLETED);
                return response;
            }

        };

        final RestfulAsyncCallback callback = new RestfulAsyncCallback() {

            @Override
            public void callback(final RestfulResponse response) {
                System.out.println("callback called.");

            }

            @Override
            public void handleExcepion(final Throwable e) {

                System.out.println("handleExcepion called.");
            }

        };
        httpRest.asyncPatch("path/to/service", new RestfulParametes(), options, callback);
        httpRest.asyncPatch("path/to/service", new RestfulParametes(), options, null);
    }

}

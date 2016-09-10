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

package org.openo.baseservice.roa.util.clientsdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import mockit.Mocked;
import mockit.NonStrictExpectations;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   13-Jun-2016
 */
public class TestHttpUtil {

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
     * @since  
     */
    @Test
    public void testContainsIgnoreCase() {
        final String[] array = {"hello", "how", "are", "you", "?"};
        final String toFind = "Hello";
        Assert.assertTrue(HttpUtil.containsIgnoreCase(array, toFind));
    }

    /**
     * <br/>
     * 
     * @since  
     */
    @Test
    public void testContainsIgnoreCaseNull() {
        final String[] array = {"hello", "how", "are", "you", "?"};
        final String toFind = "Hello";
        Assert.assertFalse(HttpUtil.containsIgnoreCase(array, null));

        array[0] = null;
        Assert.assertFalse(HttpUtil.containsIgnoreCase(array, toFind));

        Assert.assertTrue(HttpUtil.containsIgnoreCase(array, null));
        array[0] = "hello";
        array[array.length - 1] = null;
        Assert.assertTrue(HttpUtil.containsIgnoreCase(array, null));
    }

    /**
     * <br/>
     * 
     * @since  
     */
    @Test
    public void testJoin() {
        final String[] array = {"hello", "how", "are", "you", "?"};
        String actual = HttpUtil.join(array, ",");
        String expected = "hello,how,are,you,?";
        Assert.assertEquals(actual, expected);

        actual = HttpUtil.join(array, "#");
        expected = expected.replaceAll(",", "#");
        Assert.assertEquals(actual, expected);
        actual = HttpUtil.join(new String[] {}, ",");
        Assert.assertEquals(actual, "");
    }

    /**
     * <br/>
     * 
     * @since  
     */
    @Test
    public void testParameterToString() {
        // with param string.
        Object param = new String("String Param");
        String actual = HttpUtil.parameterToString(param);
        String expected = "String Param";
        Assert.assertEquals(expected, actual);

        // with param date.
        final Date date = new Date();
        param = date;
        expected = "" + date.getTime();
        actual = HttpUtil.parameterToString(param);
        Assert.assertEquals(expected, actual);

        // with param collection.
        final String[] array = {"hello", "how", "are", "you", "?"};
        param = Arrays.asList(array);
        expected = HttpUtil.join(array, ",");
        actual = HttpUtil.parameterToString(param);
        Assert.assertEquals(expected, actual);

        // with param any
        param = new Object() {

            @Override
            public String toString() {
                return "test object";
            }
        };
        expected = "test object";
        actual = HttpUtil.parameterToString(param);
        Assert.assertEquals(expected, actual);

        // with param null.
        expected = "";
        actual = HttpUtil.parameterToString(null);
        Assert.assertEquals(expected, actual);

    }

    /**
     * <br/>
     * 
     * @since  
     */
    @Test
    public void testSelectHeaderAccept() {
        final String[] accepts = {"application/json", "text/plain", "application/xml"};
        String expected = "application/json";
        String actual = HttpUtil.selectHeaderAccept(accepts);
        Assert.assertEquals(expected, actual);

        accepts[0] = "application/x-www-form-urlencoded";
        expected = HttpUtil.join(accepts, ",");
        actual = HttpUtil.selectHeaderAccept(accepts);
        Assert.assertEquals(expected, actual);

        expected = null;
        actual = HttpUtil.selectHeaderAccept(new String[] {});
        Assert.assertEquals(expected, actual);

    }

    /**
     * <br/>
     * 
     * @since  
     */
    @Test
    public void testSelectHeaderContentType() {
        final String[] accepts = {"application/json", "text/plain", "application/xml"};
        String expected = "application/json";
        String actual = HttpUtil.selectHeaderContentType(accepts);
        Assert.assertEquals(expected, actual);

        accepts[0] = "application/x-www-form-urlencoded";
        expected = "application/x-www-form-urlencoded";
        actual = HttpUtil.selectHeaderContentType(accepts);
        Assert.assertEquals(expected, actual);

        expected = "application/json";
        actual = HttpUtil.selectHeaderContentType(new String[] {});
        Assert.assertEquals(expected, actual);
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testEscapeString() throws Exception {
        final String str = "/this/url/to be encoded";
        final String actual = HttpUtil.escapeString(str);
        final String expected = "%2Fthis%2Furl%2Fto%20be%20encoded";
        Assert.assertEquals(expected, actual);
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Ignore
    @Test
    public void testEscapeStringException() throws Exception {

        final String str = "/this/url/to be encoded";
        new NonStrictExpectations() {

            @Mocked
            URLEncoder encoder;

            {
                URLEncoder.encode(str, "utf8");
                result = new UnsupportedEncodingException();
            }
        };

        final String actual = HttpUtil.escapeString(str);
        Assert.assertEquals(str, actual);
    }
}

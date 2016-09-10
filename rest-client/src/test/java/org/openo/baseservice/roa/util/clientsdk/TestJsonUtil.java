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

import org.openo.baseservice.roa.util.clientsdk.demo.JsonTestClass;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.json.JSONObject;

import junit.framework.Assert;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   13-Jun-2016
 */
public class TestJsonUtil {

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
    public void testUnMarshalStringClassOfT() throws Exception {
        final String name = "myname";
        final int id = 25;
        final String jsonstr = "{\"name\": \"" + name + "\", \"id\": " + id + "}";

        final JsonTestClass jsonObj = JsonUtil.unMarshal(jsonstr, JsonTestClass.class);

        Assert.assertNotNull(jsonObj);
        Assert.assertEquals(name, jsonObj.getName());
        Assert.assertEquals(id, jsonObj.getId());

    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testUnMarshalStringTypeReferenceOfT() throws Exception {
        final String name = "myname";
        final int id = 25;
        final String jsonstr = "{\"name\": \"" + name + "\", \"id\": " + id + "}";

        final JsonTestClass jsonObj = JsonUtil.unMarshal(jsonstr, new TypeReference<JsonTestClass>() {});

        Assert.assertNotNull(jsonObj);
        Assert.assertEquals(name, jsonObj.getName());
        Assert.assertEquals(id, jsonObj.getId());
    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testMarshal() throws Exception {
        final JsonTestClass jsonObj = new JsonTestClass();
        jsonObj.setId(1);
        jsonObj.setName("somename");
        final String str = JsonUtil.marshal(jsonObj);
        final JSONObject json = JSONObject.fromObject(str);
        Assert.assertNotNull(json);
        Assert.assertEquals(json.getString("name"), jsonObj.getName());
        Assert.assertEquals(json.getInt("id"), jsonObj.getId());

    }

    /**
     * <br/>
     * 
     * @throws Exception
     * @since  
     */
    @Test
    public void testMarshalJsonObj() throws Exception {
        final JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", 10);
        jsonObj.put("name", "some-name");
        final String str = JsonUtil.marshal(jsonObj);
        final JSONObject json = JSONObject.fromObject(str);
        Assert.assertNotNull(json);
        Assert.assertEquals(json.getString("name"), "some-name");
        Assert.assertEquals(json.getInt("id"), 10);

    }

    /**
     * <br/>
     * 
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws Exception
     * @since  
     */
    @Test
    public void testGetMapper() throws JsonParseException, JsonMappingException, Exception {
        final String name = "myname";
        final int id = 25;
        final ObjectMapper mapper = JsonUtil.getMapper();
        Assert.assertNotNull(mapper);
        final JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);
        final JsonTestClass jsonObj = mapper.readValue(json.toString(), JsonTestClass.class);
        Assert.assertNotNull(jsonObj);
        Assert.assertEquals(name, jsonObj.getName());
        Assert.assertEquals(id, jsonObj.getId());
    }
}

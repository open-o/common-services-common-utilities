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
package org.openo.baseservice.encrypt.cbb.sha;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   03-Jun-2016
 */
@RunWith(JMockit.class)
public class Sha256Test {

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
     * Test method for {@link org.openo.baseservice.encrypt.cbb.sha.Sha256#digest(java.lang.String)}
     * .
     */
    @Test
    public void testDigest() {
        String plain = "";
        String expected = "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855";
        Assert.assertEquals(expected, Sha256.digest(plain));

        expected = "D7A8FBB307D7809469CA9ABCB0082E4F8D5651E46D3CDB762D02D0BF37C9E592";
        plain = "The quick brown fox jumps over the lazy dog";
        Assert.assertEquals(expected, Sha256.digest(plain));
    }

    @Test
    public void testDigestException() throws Exception {
        new NonStrictExpectations() {

            @Mocked
            MessageDigest md;

            {
                md = MessageDigest.getInstance("SHA-256");
                result = new NoSuchAlgorithmException();
            }
        };
        final String plain = "";
        final String expected = "";
        Assert.assertEquals(expected, Sha256.digest(plain));

    }

    /**
     * Test method for
     * {@link org.openo.baseservice.encrypt.cbb.sha.Sha256#mac(java.lang.String, java.security.Key)}
     * .
     * 
     * @throws InvalidKeyException
     */
    @Test
    public void testMacStringKey() {
        final String expected = "F7BC83F430538424B13298E6AA6FB143EF4D59A14946175997479DBC2D1A3CD8";
        final String plain = "The quick brown fox jumps over the lazy dog";
        try {
            Assert.assertEquals(expected, Sha256.mac(plain, new SecretKeySpec("key".getBytes(), "HmacSHA256")));
        } catch(final InvalidKeyException e) {
            e.printStackTrace();
            fail("testMacStringKey failed" + e.getMessage());
        }
        try {
            Assert.assertEquals(expected, Sha256.mac(plain, new SecretKeySpec("key".getBytes(), "AES")));
        } catch(final InvalidKeyException e) {
            e.printStackTrace();
            fail("testMacStringKey failed" + e.getMessage());
        }

    }

    @Test
    public void testMacStringKeyException() throws Exception {
        new NonStrictExpectations() {

            @Mocked
            Mac mac;

            {
                mac = Mac.getInstance("HmacSHA256");
                result = new NoSuchAlgorithmException();
            }
        };
        Sha256.mac("dummy", new SecretKeySpec("key".getBytes(), "AES"));
    }

    /**
     * Test method for
     * {@link org.openo.baseservice.encrypt.cbb.sha.Sha256#mac(java.lang.String, byte[])}.
     */
    @Test
    public void testMacStringByteArray() {
        final String expected = "F7BC83F430538424B13298E6AA6FB143EF4D59A14946175997479DBC2D1A3CD8";
        final String plain = "The quick brown fox jumps over the lazy dog";
        Assert.assertEquals(expected, Sha256.mac(plain, "key".getBytes()));
    }

    @Test
    public void testMacStringByteArrayInvalidKeyException() throws Exception {
        final String key = "key";
        new NonStrictExpectations() {

            @Mocked
            Mac mac;

            {
                mac = Mac.getInstance("HmacSHA256");
                result = new InvalidKeyException();
            }
        };
        final String expected = "";
        final String plain = "The quick brown fox jumps over the lazy dog";
        Assert.assertEquals(expected, Sha256.mac(plain, key.getBytes()));
    }

}

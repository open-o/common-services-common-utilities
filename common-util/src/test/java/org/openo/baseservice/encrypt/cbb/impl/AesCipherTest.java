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
package org.openo.baseservice.encrypt.cbb.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.encrypt.cbb.CipherCreator;
import org.openo.baseservice.encrypt.cbb.inf.AbstractCipher;

import junit.framework.Assert;
import mockit.Mocked;
import mockit.NonStrictExpectations;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   02-Jun-2016
 */
public class AesCipherTest {

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since  
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        CipherCreator.instance().setFactory(new AesCipherFactory());
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
     * Test method for
     * {@link org.openo.baseservice.encrypt.cbb.impl.AesCipher#encrypt(java.lang.String)}.
     */
    @Test
    public void testEncrypt() {
        final AbstractCipher cipherManager = CipherCreator.instance().create();
        final String encrypted = cipherManager.encrypt("test-encrypt");
        final String decrypted = cipherManager.decrypt(encrypted);

        Assert.assertEquals("test-encrypt", decrypted);
    }

    @Test
    public void testEncryptException() throws Exception {
        new NonStrictExpectations() {

            @Mocked
            Cipher cipher;

            {
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                result = new InvalidKeySpecException();
            }
        };
        final AbstractCipher cipherManager = CipherCreator.instance().create();
        final String encrypted = cipherManager.encrypt("test-encrypt");

        Assert.assertEquals(null, encrypted);
    }

    /**
     * Test method for
     * {@link org.openo.baseservice.encrypt.cbb.impl.AesCipher#decrypt(java.lang.String)}.
     */
    @Test
    public void testDecrypt() {
        final AbstractCipher cipherManager = CipherCreator.instance().create();
        final String encrypted = cipherManager.encrypt("test-encrypt");
        final String decrypted = cipherManager.decrypt(encrypted);

        Assert.assertEquals("test-encrypt", decrypted);
    }

    @Test
    public void testDecryptNull() {
        final AbstractCipher cipherManager = CipherCreator.instance().create();
        String decrypted = cipherManager.decrypt(null);
        Assert.assertEquals(null, decrypted);

        decrypted = cipherManager.decrypt("");

        Assert.assertEquals(null, decrypted);
    }

    /**
     * Test method for
     * {@link
     * org.openo.baseservice.encrypt.cbb.impl.AesCipher#CipherManagerImpl(java.lang.String)}
     * .
     */
    @Test
    public void testCipherManagerImplString() {
        final AbstractCipher cipherManager = CipherCreator.instance().create("secret-key");
        final String encrypted = cipherManager.encrypt("test-encrypt");
        final String decrypted = cipherManager.decrypt(encrypted);

        Assert.assertEquals("test-encrypt", decrypted);
    }

    /**
     * <br/>
     * 
     * @since  
     */
    @Test
    public void testCipherManagerImplStringDiffKey() {
        final String encrypted = CipherCreator.instance().create("secret-key").encrypt("test-encrypt");
        final String decrypted = CipherCreator.instance().create("wrong-key").decrypt(encrypted);

        Assert.assertNotSame("test-encrypt", decrypted);

        final String decrypt = CipherCreator.instance().create("secret-key").decrypt(encrypted);
        Assert.assertEquals("test-encrypt", decrypt);
    }
    
    @Test
    public void testCreateSecretKeyNoSuchAlgorithmException() throws Exception {
        new NonStrictExpectations() {

            @Mocked
            SecretKeyFactory keyFactory;

            {
                keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                result = new NoSuchAlgorithmException();
            }
        };

        final AbstractCipher cipherManager = CipherCreator.instance().create("secret-key");
        final String encrypted = cipherManager.encrypt("test-encrypt");
        Assert.assertEquals(encrypted, null);

    }

    @Test
    public void testCreateSecretKeyInvalidKeySpecException() throws Exception {
        new NonStrictExpectations() {

            @Mocked
            SecretKeyFactory keyFactory;

            {
                keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                result = new InvalidKeySpecException();
            }
        };

        final AbstractCipher cipherManager = CipherCreator.instance().create("secret-key");
        final String decrypted = cipherManager.decrypt("test-encrypt");
        Assert.assertEquals(decrypted, null);

    }
}

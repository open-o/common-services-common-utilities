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
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.openo.baseservice.encrypt.cbb.inf.AbstractCipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CipherManager implement<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   31-May-2016
 */
public class AesCipher implements AbstractCipher {

    private static final Logger LOG = LoggerFactory.getLogger(AesCipher.class);
    
    private static final byte[] DEFAULT_IV = {2, 1, 4, 8, 0, 3, 2, 0, 7, 9, 2, 8, 5, 11, 6, 1};

    private static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(DEFAULT_IV);

    protected final SecretKey secretKey;

    /**
     * Constructor<br/>
     * <p>
     * Creates default key.
     * </p>
     * 
     * @since  
     */
    public AesCipher() {
        super();
        secretKey = createSecretKey("default");
    }

    /**
     * 
     * Constructor<br/>
     * <p>
     * </p>
     * Creates key.
     * @param key
     * @since   
     */
    public AesCipher(final String key) {
        super();
        secretKey = createSecretKey(key);
    }

    private SecretKey createSecretKey(final String key) {
        SecretKey secretKey = null;
        try {
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            final KeySpec keySpec = new PBEKeySpec(key.toCharArray(), DEFAULT_IV, 30000, 128);

            secretKey = keyFactory.generateSecret(keySpec);
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch(InvalidKeySpecException e) {
            LOG.error("Invalid KeySpec ", e);
        } catch(NoSuchAlgorithmException e) {
            LOG.error("Algorithm do not support: ", e);
        }
        return null;
    }

    @Override
    public String encrypt(final String plain) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV_PARAMETER_SPEC);
            final byte[] encryptToBytes = cipher.doFinal(plain.getBytes());
            return DatatypeConverter.printBase64Binary(encryptToBytes);
        } catch(final Exception e) {
            LOG.error("Encrypt the plain error:", e);
            return null;
        }
    }

    @Override
    public String decrypt(final String encrypted) {

        if(encrypted == null || encrypted.length() == 0) {
            return null;
        }

        if(secretKey == null) {
            return null;
        }

        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IV_PARAMETER_SPEC);
            final byte[] tempBytes = DatatypeConverter.parseBase64Binary(encrypted);
            final byte[] decrypTobytes = cipher.doFinal(tempBytes);
            return new String(decrypTobytes);
        } catch(final Exception e) {
            LOG.error("decrypt the plain error:", e);
            return null;
        }
    }

}

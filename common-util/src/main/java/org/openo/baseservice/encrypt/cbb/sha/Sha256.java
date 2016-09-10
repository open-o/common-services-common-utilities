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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Utility to generate SHA256 digest and HMAC.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   03-Jun-2016
 */
public final class Sha256 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sha256.class);

    private Sha256() {

    }

    /**
     * Generates SHA256 digest.<br/>
     * 
     * @param data: The data to be digested.
     * @return Hex encoded digested data.
     * @since  
     */
    public static String digest(final String data) {
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch(final NoSuchAlgorithmException e) {
            LOGGER.error("No SHA-256 support ", e);
            return "";
        }
        final byte[] digest = md.digest(dataBytes);
        return DatatypeConverter.printHexBinary(digest);
    }

    /**
     * Generates hmac signature using data and key.<br/>
     * 
     * @param data: The data to be signed.
     * @param key: The signing key.
     * @return Hex encoded HMAC signature.
     * @throws InvalidKeyException if the key is invalid.
     * @since  
     */
    public static String mac(final String data, final Key key) throws InvalidKeyException {
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
        } catch(final NoSuchAlgorithmException e) {
            LOGGER.error("SHA mac not supported", e);
            return "";
        }
        final byte[] digest = mac.doFinal(dataBytes);
        return DatatypeConverter.printHexBinary(digest);

    }

    /**
     * Generates hmac with data and secret.
     * <br/>
     * 
     * @param data: The data to be signed.
     * @param secret: The signing key.
     * @return Hex encoded HMAC signature.
     * @since  
     */
    public static String mac(final String data, final byte[] secret) {
        final Key key = new SecretKeySpec(secret, "HmacSHA256");
        try {
            return mac(data, key);
        } catch(final InvalidKeyException e) {
            LOGGER.error("Invalid key: ", e);
            return "";
        }
    }

}

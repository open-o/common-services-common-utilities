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
package org.openo.baseservice.encrypt.cbb.inf;

/**
 * Cipher Manager, provides the encrypt/decrypt interface
 * <br/>
 * <p>
 * It provides bi-directional encryption api.
 * </p>
 * 
 * @author
 * @version   31-May-2016
 */
public interface AbstractCipher {

    /**
     * Encrypt a string.
     * <br/>
     * 
     * @param plain string to be encrypted.
     * @return encrypted string.
     * @since  
     */
    String encrypt(String plain);

    /**
     * Decrypt a string.
     * <br/>
     * 
     * @param encrypted String is encrypted by AES 128
     * @return plain after decrypt
     * @since  
     */
    String decrypt(String encrypted);

}

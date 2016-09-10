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
package org.openo.baseservice.encrypt.cbb;

import org.openo.baseservice.encrypt.cbb.impl.AesCipherFactory;
import org.openo.baseservice.encrypt.cbb.inf.AbstractCipher;
import org.openo.baseservice.encrypt.cbb.inf.AbstractCipherFactory;

/**
 * Helps create cipher instances from factory.<br/>
 * <p>
 * Creates the cipher instances using cipher factory. By default it uses AesCipherFactory.
 * Can be changed through spring.
 * </p>
 * 
 * @author
 * @version   08-Jun-2016
 */
public final class CipherCreator {

    private static CipherCreator instance = new CipherCreator();

    private AbstractCipherFactory factory = new AesCipherFactory();

    /**
     * Constructor<br/>
     * <p>
     * private
     * </p>
     * 
     * @since  
     */
    private CipherCreator() {

    }

    /**
     * Singleton instance.
     * <br/>
     * 
     * @return
     * @since  
     */
    public static CipherCreator instance() {
        return instance;
    }

    /**
     * Creates cipher with default key.
     * <br/>
     * 
     * @return cipher instance with default key.
     * @since  
     */
    public AbstractCipher create() {
        return factory.createCipherManager();
    }

    /**
     * Creates cipher instance with a key.
     * <br/>
     * 
     * @param key the key to be used for encryption and decryption.
     * @return cipher instance with specified key.
     * @since  
     */
    public AbstractCipher create(final String key) {
        return factory.createCipherManager(key);
    }

    /**
     * Sets the cipher factory instance.
     * <br/>
     * 
     * @param factory cipher factory.
     * @since  
     */
    public void setFactory(final AbstractCipherFactory factory) {
        this.factory = factory;
    }

}

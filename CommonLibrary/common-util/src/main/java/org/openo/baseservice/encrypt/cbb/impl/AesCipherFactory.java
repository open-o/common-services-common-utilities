/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.baseservice.encrypt.cbb.impl;

import org.openo.baseservice.encrypt.cbb.inf.AbstractCipher;
import org.openo.baseservice.encrypt.cbb.inf.AbstractCipherFactory;

/**
 * Factory class to create CipherManager instances.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version SDNO 0.5 03-Jun-2016
 */
public class AesCipherFactory implements AbstractCipherFactory {

    /**
     * Creates new CipherManager instance.<br/>
     * 
     * @return new cipher manager instance.
     * @since SDNO 0.5
     */
    @Override
    public AbstractCipher createCipherManager() {
        return new AesCipher();
    }

    /**
     * Creates new CipherManager instance.<br/>
     * 
     * @param key new cipher manager instance.
     * @return
     * @since SDNO 0.5
     */
    @Override
    public AbstractCipher createCipherManager(final String key) {
        return new AesCipher(key);
    }

}

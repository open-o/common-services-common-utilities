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
 * Factory class to create CipherManager instances.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version   03-Jun-2016
 */
public interface AbstractCipherFactory {

    /**
     * Creates new CipherManager instance.<br/>
     * 
     * @return new cipher manager instance.
     * @since  
     */
    AbstractCipher createCipherManager();

    /**
     * Creates new CipherManager instance.<br/>
     * 
     * @param key new cipher manager instance.
     * @return
     * @since  
     */
    AbstractCipher createCipherManager(final String key);

}

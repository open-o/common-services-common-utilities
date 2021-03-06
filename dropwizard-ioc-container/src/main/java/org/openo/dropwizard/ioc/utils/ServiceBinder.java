/**
 * Copyright 2017 ZTE Corporation.
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
package org.openo.dropwizard.ioc.utils;

import java.util.Set;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hu.rui
 *
 */
public class ServiceBinder extends AbstractBinder {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServiceBinder.class);
	
	final Set<Class<?>> klasses;

	public ServiceBinder(Set<Class<?>> services) {
		this.klasses = services;
	}

	@Override
	protected void configure() {
		for (Class<?> klass : this.klasses) {
			
			try{
				LOG.info("start active class:"+klass.getName());
				addActiveDescriptor(klass);
			}catch(Exception e){
				LOG.info("active class error:"+klass.getName(),e);
			}
			
			
		}
	}
}

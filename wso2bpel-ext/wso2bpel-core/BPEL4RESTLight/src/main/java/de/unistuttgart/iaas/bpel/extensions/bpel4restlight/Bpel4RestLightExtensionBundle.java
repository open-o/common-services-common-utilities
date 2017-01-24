/**
 * Copyright 2011 IAAS University of Stuttgart <br>
 * <br>
 * 
 * @author uwe.breitenbuecher@iaas.uni-stuttgart.de
 * 
 */
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
package de.unistuttgart.iaas.bpel.extensions.bpel4restlight;

import org.apache.ode.bpel.runtime.extension.AbstractExtensionBundle;


public class Bpel4RestLightExtensionBundle extends AbstractExtensionBundle {
	
	public static final String NAMESPACE = "http://iaas.uni-stuttgart.de/bpel/extensions/bpel4restlight";
	
	
	/** {@inheritDoc} */
	@Override
	public String getNamespaceURI() {
		return NAMESPACE;
	}
	
	/** {@inheritDoc} */
	@Override
	public void registerExtensionActivities() {
		super.registerExtensionOperation("logNodes", EPRDemoOperation.class);
		super.registerExtensionOperation("PUT", Bpel4RestLightOperation.class);
		super.registerExtensionOperation("GET", Bpel4RestLightOperation.class);
		super.registerExtensionOperation("POST", Bpel4RestLightOperation.class);
		super.registerExtensionOperation("DELETE", Bpel4RestLightOperation.class);
	}
}

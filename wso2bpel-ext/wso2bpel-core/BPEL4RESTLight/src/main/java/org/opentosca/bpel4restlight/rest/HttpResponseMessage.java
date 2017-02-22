/*******************************************************************************
 * Copyright (c) 2012-2013 University of Stuttgart.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and the Apache License 2.0 which both accompany this distribution,
 * and are available at http://www.eclipse.org/legal/epl-v10.html
 * and http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/*
 * Modifications Copyright 2016-2017 ZTE Corporation.
 */
/**
 * This enum is intended to simplify identifying different HTTP-methods
 */
package org.opentosca.bpel4restlight.rest;

/**
 */
public class HttpResponseMessage {
	
	private int statusCode;
	private String responseBody;
	
	
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return this.statusCode;
	}
	
	/**
	 * @param statusCode the statusCode to set
	 */
	protected void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * @return the responseBody
	 */
	public String getResponseBody() {
		return this.responseBody;
	}
	
	/**
	 * @param responseBody the responseBody to set
	 */
	protected void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	
}

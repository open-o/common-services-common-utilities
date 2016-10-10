/**
 * Copyright 2016 ZTE Corporation.
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
package org.openo.carbon.bpel.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import org.apache.log4j.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;

public class ClientWsdlLoader extends WsdlLoader {
  private static Logger logger = Logger.getLogger(ClientWsdlLoader.class);

  private boolean isAborted = false;
  private HttpClient httpClient;

  public ClientWsdlLoader(String url, HttpClient httpClient) {
    super(url);
    this.httpClient = httpClient;
  }

  public InputStream load(String url) throws Exception {
    GetMethod httpGetMethod;

    if (url.startsWith("file")) {
      return new URL(url).openStream();
    }

    // Authentication is not being overridden on the method. It needs
    // to be present on the supplied HttpClient instance!
    httpGetMethod = new GetMethod(url);
    httpGetMethod.setDoAuthentication(true);

    try {
      int result = httpClient.executeMethod(httpGetMethod);

      if (result != HttpStatus.SC_OK) {
        if (result < 200 || result > 299) {
          throw new HttpException(
              "Received status code '" + result + "' on WSDL HTTP (GET) request: '" + url + "'.");
        } else {
          logger.warn(
              "Received status code '" + result + "' on WSDL HTTP (GET) request: '" + url + "'.");
        }
      }

      return new ByteArrayInputStream(httpGetMethod.getResponseBody());
    } finally {
      httpGetMethod.releaseConnection();
    }
  }

  public boolean abort() {
    isAborted = true;
    return true;
  }

  public boolean isAborted() {
    return isAborted;
  }

  public void close() {}
}

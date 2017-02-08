/**
 * Copyright 2017 ZTE Corporation.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.baseservice.i18n;

public class ErrorCodeException extends Exception {

  private static final long serialVersionUID = 5522808473663280894L;

  private int errorCode;

  /**
   * Error definition placeholder replacement
   */
  private String[] arguments;

  /**
   * According to the original exception, error code, debug print information and construct a new
   * anomaly placeholder
   *
   * @param source
   * @param errorCode
   * @param debugMessage
   * @param arguments
   */
  public ErrorCodeException(Throwable source, int errorCode, String debugMessage,
      String[] arguments) {
    super(debugMessage, source);

    this.errorCode = errorCode;

    this.arguments = arguments;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public String[] getArguments() {
    return arguments;
  }

}

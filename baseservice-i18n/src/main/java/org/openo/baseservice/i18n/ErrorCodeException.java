/**
 * Copyright 2016 ZTE Corporation.
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

  /**
   * 
   */
  private static final long serialVersionUID = 5522808473663280894L;

  /**
   * 错误码
   */
  private int errorCode;

  /**
   * 错误定义占位替换
   */
  private String[] arguments;

  /**
   * 根据原始异常、错误码、调试打印信息和占位符构造新的异常
   * 
   * @param source 原始异常
   * @param errorCode 错误码
   * @param debugMessage 调试打印信息
   * @param arguments 占位符
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

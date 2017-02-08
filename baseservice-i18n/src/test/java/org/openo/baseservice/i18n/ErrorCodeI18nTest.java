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

import java.util.Locale;

public class ErrorCodeI18nTest {
  public static void main(String[] args) {
    ErrorCodeI18n.getInstance().getErrorItem(53501).ifPresent(errorCodeI18n -> {
      int code = errorCodeI18n.getErrorCode();
      System.out.println("getErrorCode is:" + code);
      System.out.println("getErrorCode is:" + errorCodeI18n.getLabel(Locale.CHINA));
      System.out.println("APITEST:I18N_getErrorCode_success!");
    });

    // byte[] bs=new byte[Byte.MAX_VALUE-Byte.MIN_VALUE];
    // System.out.println(bs.length);
    //
    // int i=0;
    // for(byte b=Byte.MIN_VALUE;b<Byte.MAX_VALUE;b++){
    // bs[i++]=b;
    // }
    // System.out.println(Arrays.toString(bs));
    //
    // byte[] ubs=null;
    // if(bs.length>3){
    // //删除bom头 -17, -69, -65
    //// if(bs[0]==-17&&bs[1]==-69&&bs[2]==-65){
    // ubs=new byte[bs.length-3];
    // System.arraycopy(bs, 3, ubs, 0, ubs.length);
    //// }
    // }
    // System.out.println(Arrays.toString(ubs));

    System.out.println("Over..");
  }
}

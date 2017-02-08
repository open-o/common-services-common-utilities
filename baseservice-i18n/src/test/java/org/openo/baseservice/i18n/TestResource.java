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

import org.jvnet.hk2.annotations.Service;
import org.openo.baseservice.i18n.ErrorCodeI18n.ErrorItem;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Optional;

@Path("/test")
@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
@Service
public class TestResource {


  @Inject
  I18nService i18nService;

  public TestResource() {
    System.out.println("aaa");
  }

  /// http://127.0.0.1:9090/test/i18n?i18n=fm&label=fm_uca
  @Path("/i18n")
  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  public String i18n(@QueryParam(value = "i18n") String i18nName,
      @QueryParam(value = "label") String label) {
    StringBuilder sb = new StringBuilder("sucess");
    i18nService.getI18n(i18nName).ifPresent(i18n -> {
      sb.append(" value:" + i18n.getCanonicalLabelValues(label));
    });
    return sb.toString();
  }

  // http://127.0.0.1:9090/test/errorcode?code=53505
  @Path("/errorcode")
  @GET
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  public String errorcode(@QueryParam(value = "code") Integer code) {
    StringBuilder sb = new StringBuilder("sucess");
    Optional<ErrorItem> optional = i18nService.getErrorItem(code);
    optional.ifPresent(errorItem -> {
      sb.append(" value:" + errorItem.getCanonicalLabels(1));
    });
    return sb.toString();
  }
}

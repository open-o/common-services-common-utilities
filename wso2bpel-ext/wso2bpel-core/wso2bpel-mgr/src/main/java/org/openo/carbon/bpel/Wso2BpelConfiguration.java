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
package org.openo.carbon.bpel;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class Wso2BpelConfiguration extends Configuration {
  @NotEmpty
  private String template;
  @NotEmpty
  private String apiDescription = "Wso2bps REST API";
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();

  @JsonProperty
  public String getTemplate() {
    return template;
  }

  @JsonProperty
  public String getApiDescription() {
    return apiDescription;
  }
  
  @NotEmpty
  private String msbServerAddr;

  @JsonProperty("database")
  public DataSourceFactory getDataSourceFactory() {
    return database;
  }

  @JsonProperty
  public DataSourceFactory getDatabase() {
    return database;
  }

  @JsonProperty
  public void setDatabase(DataSourceFactory database) {
    this.database = database;
  }

  @JsonProperty
  public String getMsbServerAddr() {
    return msbServerAddr;
  }

  @JsonProperty
  public void setMsbServerAddr(String msbServerAddr) {
    this.msbServerAddr = msbServerAddr;
  }

}

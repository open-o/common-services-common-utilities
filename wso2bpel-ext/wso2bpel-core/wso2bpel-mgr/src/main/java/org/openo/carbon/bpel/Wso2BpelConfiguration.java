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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Wso2BpelConfiguration extends Configuration {
  @NotEmpty
  private String template;
  @NotEmpty
  private String apiDescription = "Wso2bps REST API";
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();
  
  @NotEmpty
  private String msbServerAddr;
  
  @NotEmpty
  private String wso2Host;

  @NotEmpty
  private String wso2HostPort;
  
  @NotEmpty
  private String wso2AuthUserName;
  
  @NotEmpty
  private String wso2AuthPassword;
  
  @NotEmpty
  private String wso2Path;
  
  @NotEmpty
  private String wso2UploadFilePath;
  
  @NotEmpty
  private String wso2SslJksFile;
  
  @NotEmpty
  private String wso2SslJksPassword;
  
  @Valid
  private String serviceIp;
  
  @JsonProperty
  public String getTemplate() {
    return template;
  }

  @JsonProperty
  public String getApiDescription() {
    return apiDescription;
  }

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
  
  @JsonProperty
  public String getWso2Host() {
    return wso2Host;
  }

  @JsonProperty
  public void setWso2Host(String wso2Host) {
    this.wso2Host = wso2Host;
  }
  
  @JsonProperty
  public String getWso2HostPort() {
    return wso2HostPort;
  }

  @JsonProperty
  public void setWso2HostPort(String wso2HostPort) {
    this.wso2HostPort = wso2HostPort;
  }

  @JsonProperty
  public String getWso2AuthUserName() {
    return wso2AuthUserName;
  }

  @JsonProperty
  public void setWso2AuthUserName(String wso2AuthUserName) {
    this.wso2AuthUserName = wso2AuthUserName;
  }

  @JsonProperty
  public String getWso2AuthPassword() {
    return wso2AuthPassword;
  }

  @JsonProperty
  public void setWso2AuthPassword(String wso2AuthPassword) {
    this.wso2AuthPassword = wso2AuthPassword;
  }

  @JsonProperty
  public String getWso2Path() {
    return wso2Path;
  }

  @JsonProperty
  public void setWso2Path(String wso2Path) {
    this.wso2Path = wso2Path;
  }

  @JsonProperty
  public String getWso2UploadFilePath() {
    return wso2UploadFilePath;
  }

  @JsonProperty
  public void setWso2UploadFilePath(String wso2UploadFilePath) {
    this.wso2UploadFilePath = wso2UploadFilePath;
  }

  @JsonProperty
  public String getWso2SslJksFile() {
    return wso2SslJksFile;
  }

  @JsonProperty
  public void setWso2SslJksFile(String wso2SslJksFile) {
    this.wso2SslJksFile = wso2SslJksFile;
  }

  @JsonProperty
  public String getWso2SslJksPassword() {
    return wso2SslJksPassword;
  }

  @JsonProperty
  public void setWso2SslJksPassword(String wso2SslJksPassword) {
    this.wso2SslJksPassword = wso2SslJksPassword;
  }

  @JsonProperty
  public String getServiceIp() {
    return serviceIp;
  }

  @JsonProperty
  public void setServiceIp(String serviceIp) {
    this.serviceIp = serviceIp;
  }
}

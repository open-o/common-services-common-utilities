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

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.openo.carbon.bpel.common.Config;
import org.openo.carbon.bpel.common.ServiceRegistrer;
import org.openo.carbon.bpel.resources.BpsPackage;
import org.openo.carbon.bpel.resources.BpsProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;



public class Wso2BpelApplication extends Application<Wso2BpelConfiguration> {
  private static final Logger LOGGER = LoggerFactory.getLogger(Wso2BpelApplication.class);
  private static final String API_RESOURCE = "org.openo.carbon.bpel.resources";

  public static void main(String[] args) throws Exception {
    new Wso2BpelApplication().run(args);
  }


  @Override
  public String getName() {
    return "hello-wso2bpel";
  }

  @Override
  public void initialize(Bootstrap<Wso2BpelConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
        bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
    // binding jar static resource
    bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
  }
  
  private void initService() {
    Thread registerWso2bpelService = new Thread(new ServiceRegistrer());
    registerWso2bpelService.setName("register wso2bpel service to Microservice Bus");
    registerWso2bpelService.start();
  }



  @Override
  public void run(Wso2BpelConfiguration configuration, Environment environment) {
    environment.jersey().register(MultiPartFeature.class);
    environment.jersey().register(new BpsPackage());
    environment.jersey().register(new BpsProcess());
    // init Swagger conf
    initSwaggerConfig(environment, configuration);
    Config.setConfigration(configuration);
    initService();
  }

  private void initSwaggerConfig(Environment environment, Wso2BpelConfiguration configuration) {
    // register swagger scan class
    environment.jersey().register(new ApiListingResource());
    environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    BeanConfig config = new BeanConfig();
    config.setTitle(configuration.getApiDescription());
    config.setVersion("1.0");
    config.setResourcePackage(API_RESOURCE);

    // read rootPath config from yml
    SimpleServerFactory simpleServerFactory =
        (SimpleServerFactory) configuration.getServerFactory();
    String basePath = simpleServerFactory.getApplicationContextPath();
    String rootPath = simpleServerFactory.getJerseyRootPath();

    // set basepath for rest api
    rootPath = rootPath.substring(0, rootPath.indexOf("/*"));
    basePath = basePath.equals("/") ? rootPath
        : (new StringBuilder()).append(basePath).append(rootPath).toString();

    LOGGER.info("getApplicationContextPath:" + basePath);
    config.setBasePath(basePath);
    config.setScan(true);
  }
}

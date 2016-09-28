/**
 * Copyright 2016 [ZTE] and others.
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
package org.openo.carbon.bpel.common;

import org.openo.carbon.bpel.externalservice.entity.ServiceRegisterEntity;
import org.openo.carbon.bpel.externalservice.msb.MicroserviceBusConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRegistrer implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(ServiceRegistrer.class);
  private final ServiceRegisterEntity wso2bpelEntity = new ServiceRegisterEntity();

  public ServiceRegistrer() {
    initServiceEntity();
  }

  @Override
  public void run() {
    LOG.info("start wso2bpelEntity microservice register");
    boolean flag = false;
    int retry = 0;
    while (!flag && retry < 1000) {
      LOG.info("wso2bpel microservice register.retry:" + retry);
      retry++;
      flag = MicroserviceBusConsumer.registerService(wso2bpelEntity);
      if (flag == false) {
        LOG.warn("microservice register failed, sleep 30S and try again.");
        threadSleep(30000);
      } else {
        LOG.info("microservice register success!");
        break;
      }
    }
    LOG.info("wso2bpel microservice register end.");
  }

  private void threadSleep(int second) {
    LOG.info("start sleep ....");
    try {
      Thread.sleep(second);
    } catch (InterruptedException error) {
      LOG.error("thread sleep error.errorMsg:" + error.getMessage());
    }
    LOG.info("sleep end .");
  }

  private void initServiceEntity() {
    wso2bpelEntity.setServiceName("wso2bpel");
    wso2bpelEntity.setProtocol("REST");
    wso2bpelEntity.setVersion("v1");
    wso2bpelEntity.setUrl("/openoapi/wso2bpel/v1");
    wso2bpelEntity.setSingleNode(null, "8101", 0);
    wso2bpelEntity.setVisualRange("1");
  }

}

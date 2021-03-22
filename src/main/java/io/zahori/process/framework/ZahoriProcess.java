package io.zahori.process.framework;

/*-
 * #%L
 * zahori-process
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2021 PANEL SISTEMAS INFORMATICOS,S.L
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import io.zahori.framework.core.BaseProcess;
import io.zahori.model.process.CaseExecution;
import io.zahori.model.process.ProcessRegistration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *************************************************************************************
 * Zahori process. Please, don't edit this class unless you know what you are doing :)
 *************************************************************************************
 */

@RestController
@RequestMapping(BaseProcess.BASE_URL)
public abstract class ZahoriProcess extends BaseProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ZahoriProcess.class);

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Value("${zahori.process.name}")
    private String name;

    @Value("${zahori.process.clientId}")
    private Long clientId;

    @Value("${zahori.process.teamId}")
    private Long teamId;

    @Value("${zahori.process.procTypeId}")
    private Long procTypeId;

    @Value("${zahori.remote}")
    private String remote;

    @Value("${zahori.selenoid.url}")
    private String selenoidUrl;

    @Value("${zahori.server.host}")
    private String zahoriServerHost;
        
    @GetMapping
    public String healthcheck() {
        return super.healthcheck(name);
    }

    @PostMapping(BaseProcess.RUN_URL)
    public CaseExecution runProcess(@RequestBody CaseExecution caseExecution) {
        ProcessRegistration processRegistration = new ProcessRegistration(name, clientId, teamId, procTypeId);
        return super.runProcess(caseExecution, processRegistration, getServerUrl(), remote, selenoidUrl);
    }

    @EventListener
    private void onApplicationEvent(ApplicationReadyEvent event) {
        LOG.info("============== PROCESS STARTED ==============");
        String baseUrl = getServerUrl();
        LOG.info("Zahori server uri: {}", baseUrl);

        String serverStatus = new RestTemplate().getForObject(baseUrl + BaseProcess.ZAHORI_SERVER_HEALTHCHECK_URL, String.class);
        LOG.info("Zahori server status: {}", serverStatus);

        // Register process in server
        ProcessRegistration processRegistration = new ProcessRegistration(name, clientId, teamId, procTypeId);
        ResponseEntity<ProcessRegistration> processRegistrationResponse = new RestTemplate()
                .postForEntity(baseUrl + BaseProcess.ZAHORI_SERVER_PROCESS_REGISTRATION_URL, processRegistration, ProcessRegistration.class);
        processRegistrationResponse.getStatusCode();
        LOG.info("Process registration - status: {}", processRegistrationResponse.getStatusCode());
        LOG.info("Process registration - processId: {}", processRegistrationResponse.getBody().getProcessId());
    }

    private String getServerUrl() {
        ServiceInstance serviceInstance = loadBalancer.choose(BaseProcess.ZAHORI_SERVER_SERVICE_NAME);
        if (serviceInstance == null) {
            serviceInstance = waitZahoriServer(loadBalancer);
        }
        String host = StringUtils.isBlank(zahoriServerHost) ? serviceInstance.getUri().getHost() : zahoriServerHost;
        return serviceInstance.getUri().getScheme() + "://" + host + ":" + serviceInstance.getUri().getPort();
    }

    private ServiceInstance waitZahoriServer(LoadBalancerClient loadBalancer) {
        ServiceInstance serviceInstance = null;
        LOG.warn("Zahori server seems to be down or started just a few seconds ago and is still registering in eureka server");
        for (int i = 1; i <= BaseProcess.MAX_RETRIES_WAIT_FOR_SERVER; i++) {
            LOG.warn("Waiting " + BaseProcess.SECONDS_WAIT_FOR_SERVER + " seconds before retrying again...");
            pause(BaseProcess.SECONDS_WAIT_FOR_SERVER);
            serviceInstance = loadBalancer.choose(BaseProcess.ZAHORI_SERVER_SERVICE_NAME);
            if (serviceInstance != null){
                return serviceInstance;
            }
            if (serviceInstance == null && i >= BaseProcess.MAX_RETRIES_WAIT_FOR_SERVER) {
                String errorMessage = "Timeout waiting for Zahori server. Is it started and registered in eureka?";
                LOG.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
        return serviceInstance;
    }
}

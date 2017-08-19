package com.barclays.cobalt.security.tokenretriever;

import com.barclays.cobalt.security.tokenretriever.config.ApplicationProperties;
import com.barclays.cobalt.security.tokenretriever.service.ShutdownService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RetrieveTokenTask implements CommandLineRunner {

  private final RestTemplate http;
  private final ApplicationProperties properties;
  private final ShutdownService shutdownService;
  private static final Logger logger = LoggerFactory.getLogger(RetrieveTokenTask.class);

  public RetrieveTokenTask(RestTemplate restTemplate,
                           ApplicationProperties properties,
                           ShutdownService shutdownService) {
    this.properties = properties;
    this.shutdownService = shutdownService;
    this.http = restTemplate;
  }

  @Override
  public void run(String... args) throws Exception {
    String tokenEndpoint = UriComponentsBuilder.fromPath("/token")
        .queryParam("podNamespace", properties.getPodNamespace())
        .queryParam("podName", properties.getPodName())
        .build().toUriString();

    logger.info("Initiating token request to vault-controller at {}", tokenEndpoint);
    ResponseEntity<Object> status = http.postForEntity(tokenEndpoint, null, Object.class);
    HttpStatus statusCode = status.getStatusCode();
    if (statusCode.is2xxSuccessful()) {
      shutdownService.delayed();
    } else {
      logger.error("Failed to initiate request to generate secrets token." +
          " Controller returned http code: {}. Initiating shutdown.", statusCode);
      shutdownService.abnormally();
    }
  }
}

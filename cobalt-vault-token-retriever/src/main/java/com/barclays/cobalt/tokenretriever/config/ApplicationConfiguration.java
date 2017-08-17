package com.barclays.cobalt.tokenretriever.config;

import com.barclays.cobalt.tokenretriever.RetrieveTokenTask;
import com.barclays.cobalt.tokenretriever.service.ShutdownService;
import com.barclays.cobalt.tokenretriever.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {

  public static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

  @Bean
  @ConditionalOnProperty(prefix = "token-retriever", value = "token.enabled", havingValue = "true")
  public RetrieveTokenTask startupTask(RestTemplateBuilder builder,
                                       ApplicationProperties properties,
                                       ShutdownService shutdownService) {

    logger.info("Vault Controller root uri {}", properties.getVaultControllerBaseUri());
    RestTemplate restTemplate = builder
        .rootUri(properties.getVaultControllerBaseUri())
        .build();
    return new RetrieveTokenTask(restTemplate, properties, shutdownService);
  }

  @Bean
  public TokenService tokenService(RestTemplateBuilder builder, ShutdownService shutdown,
                                   ApplicationProperties properties) {
    return new TokenService(builder, shutdown, properties);
  }

  @Bean
  public ShutdownService shutdownService(ApplicationContext context, ApplicationProperties properties) {
    return new ShutdownService(context, properties.getCallbackTimeout());
  }
}

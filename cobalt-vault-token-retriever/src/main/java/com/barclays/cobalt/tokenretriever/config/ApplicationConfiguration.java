package com.barclays.cobalt.tokenretriever.config;

import com.barclays.cobalt.tokenretriever.RetrieveTokenTask;
import com.barclays.cobalt.tokenretriever.domain.TokenRetriever;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "token-retriever", value = "token.enabled", havingValue = "true")
  public RetrieveTokenTask startupTask(RestTemplateBuilder builder, ApplicationProperties properties) {
    return new RetrieveTokenTask(builder, properties);
  }

  @Bean
  public TokenRetriever tokenRetriever(RestTemplateBuilder builder, ApplicationProperties properties) {
    return new TokenRetriever(builder, properties.getVault().unwrapEndpoint(), properties.tokenPath());
  }

}

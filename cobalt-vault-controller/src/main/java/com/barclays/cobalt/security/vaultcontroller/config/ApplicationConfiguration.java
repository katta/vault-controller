package com.barclays.cobalt.security.vaultcontroller.config;

import com.barclays.cobalt.security.vaultcontroller.service.OpenshiftClient;
import com.barclays.cobalt.security.vaultcontroller.service.TokenGenerator;
import com.barclays.cobalt.security.vaultcontroller.service.PodCallbackClient;
import com.barclays.cobalt.security.vaultcontroller.service.TokenService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
  @Bean
  public TokenGenerator tokenGenerator(RestTemplateBuilder builder, ApplicationProperties properties) {
    return new TokenGenerator(builder, properties.getVault());
  }

  @Bean
  public PodCallbackClient podCallbackClient(RestTemplateBuilder builder) {
    return new PodCallbackClient(builder);
  }

  @Bean
  public OpenshiftClient openshiftClient(ApplicationProperties properties) {
    return new OpenshiftClient(properties);
  }

  @Bean
  public TokenService tokenService(TokenGenerator tokenGenerator, OpenshiftClient openshiftClient, PodCallbackClient podCallbackClient) {
    return new TokenService(tokenGenerator, openshiftClient, podCallbackClient);
  }
}

package com.barclays.cobalt.vaultcontroller.config;

import com.barclays.cobalt.vaultcontroller.domain.OpenshiftClient;
import com.barclays.cobalt.vaultcontroller.domain.PodCallbackClient;
import com.barclays.cobalt.vaultcontroller.domain.TokenGenerator;
import com.barclays.cobalt.vaultcontroller.service.TokenService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
  @Bean
  public TokenGenerator tokenGenerator(RestTemplateBuilder builder, VaultProperties configuration) {
    return new TokenGenerator(builder, configuration);
  }

  @Bean
  public PodCallbackClient podCallbackClient(RestTemplateBuilder builder) {
    return new PodCallbackClient(builder);
  }

  @Bean
  public OpenshiftClient openshiftClient(OpenshiftProperties openshiftProperties) {
    return new OpenshiftClient(openshiftProperties);
  }

  @Bean
  public TokenService tokenService(TokenGenerator tokenGenerator, OpenshiftClient openshiftClient, PodCallbackClient podCallbackClient) {
    return new TokenService(tokenGenerator, openshiftClient, podCallbackClient);
  }
}

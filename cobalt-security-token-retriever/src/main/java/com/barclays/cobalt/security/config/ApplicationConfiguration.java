package com.barclays.cobalt.security.config;

import com.barclays.cobalt.security.StartupTask;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public StartupTask startupTask(RestTemplateBuilder builder, ApplicationProperties properties) {
    return new StartupTask(builder, properties);
  }

}

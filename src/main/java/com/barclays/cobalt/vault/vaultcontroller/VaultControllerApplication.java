package com.barclays.cobalt.vault.vaultcontroller;

import com.barclays.cobalt.vault.vaultcontroller.config.VaultConfiguration;
import com.barclays.cobalt.vault.vaultcontroller.domain.TokenGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(VaultConfiguration.class)
public class VaultControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaultControllerApplication.class, args);
	}

	@Bean
	public TokenGenerator tokenGenerator(RestTemplateBuilder builder, VaultConfiguration configuration) {
		return new TokenGenerator(builder, configuration);
	}
}

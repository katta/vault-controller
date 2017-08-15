package com.barclays.cobalt.vault.vaultcontroller;

import com.barclays.cobalt.vault.vaultcontroller.config.OpenshiftProperties;
import com.barclays.cobalt.vault.vaultcontroller.config.VaultProperties;
import com.barclays.cobalt.vault.vaultcontroller.domain.PodCallbackClient;
import com.barclays.cobalt.vault.vaultcontroller.domain.TokenGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(value = {OpenshiftProperties.class, VaultProperties.class})
public class VaultControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaultControllerApplication.class, args);
	}

	@Bean
	public TokenGenerator tokenGenerator(RestTemplateBuilder builder, VaultProperties configuration) {
		return new TokenGenerator(builder, configuration);
	}

	@Bean
	public PodCallbackClient podCallbackClient(RestTemplateBuilder builder) {
		return new PodCallbackClient(builder);
	}
}

package com.barclays.cobalt.vault.vaultcontroller.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static com.barclays.cobalt.vault.vaultcontroller.domain.VaultHttpRequestInterceptor.TOKEN_HEADER;
import static com.barclays.cobalt.vault.vaultcontroller.domain.VaultHttpRequestInterceptor.WRAP_RESPONSE_TTL_HEADER;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(TokenGenerator.class)
public class TokenGeneratorTest {

  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private TokenGenerator tokenGenerator;

  @Test
  public void shouldGeneratedWrappedResponseForTokenCreationInVault() {
    server.expect(requestTo("https://localhost:8200/v1/auth/token/create"))
          .andExpect(header(TOKEN_HEADER, "vault-root-token"))
          .andExpect(header(WRAP_RESPONSE_TTL_HEADER, "60s"))
          .andExpect(jsonPath("policies").value(hasItems("non-default-policy")))
          .andRespond(withSuccess());

    tokenGenerator.generateToken("4299", "client", "non-default-policy");
  }
}
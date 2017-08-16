package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.barclays.cobalt.vault.vaultcontroller.config.VaultProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
  private VaultProperties vaultProperties;

  @Autowired
  private RestTemplateBuilder builder;

  private TokenGenerator tokenGenerator;

  @Before
  public void setUp() throws Exception {
    tokenGenerator = new TokenGenerator(builder, vaultProperties);
  }

  @Test
  public void shouldGeneratedWrappedResponseForTokenCreationInVault() {
    server.expect(requestTo("http://localhost:8200/v1/auth/token/create"))
          .andExpect(header(TOKEN_HEADER, "change-me"))
          .andExpect(header(WRAP_RESPONSE_TTL_HEADER, "60s"))
          .andExpect(jsonPath("policies").value(hasItems("non-default-policy")))
          .andRespond(withSuccess());

    tokenGenerator.generateToken("4299", "client", "non-default-policy");
  }
}
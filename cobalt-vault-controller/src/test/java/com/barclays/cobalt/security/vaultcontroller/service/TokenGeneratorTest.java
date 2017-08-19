package com.barclays.cobalt.security.vaultcontroller.service;

import com.barclays.cobalt.security.vaultcontroller.config.ApplicationProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(TokenGenerator.class)
public class TokenGeneratorTest {

  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private ApplicationProperties properties;

  @Autowired
  private RestTemplateBuilder builder;

  private TokenGenerator tokenGenerator;

  @Before
  public void setUp() throws Exception {
    tokenGenerator = new TokenGenerator(builder, properties.getVault());
  }

  @Test
  public void shouldGeneratedWrappedResponseForTokenCreationInVault() {
    server.expect(requestTo("http://localhost:8200/v1/auth/token/create"))
          .andExpect(header(TokenGenerator.TOKEN_HEADER, "change-me"))
          .andExpect(header(TokenGenerator.WRAP_RESPONSE_TTL_HEADER, "3600s"))
          .andExpect(jsonPath("policies").value(hasItems("non-default-policy")))
          .andRespond(withSuccess());

    tokenGenerator.generateToken("4299", "client", "non-default-policy");
  }
}
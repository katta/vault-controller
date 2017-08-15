package com.barclays.cobalt.vault.vaultcontroller.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@RestClientTest(PodCallbackClient.class)
public class PodCallbackClientTest {

  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private PodCallbackClient podCallbackClient;

  @Test
  public void shouldGeneratedWrappedResponseForTokenCreationInVault() {
    server.expect(
        requestTo("https://pod-ip:8999/wrapped-token"))
        .andExpect(method(POST))
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andRespond(withStatus(CREATED));

    VaultWrappedResponse.WrapInfo wrapInfo = new VaultWrappedResponse.WrapInfo("token-value", "wrapped-accessor-value", "");
    podCallbackClient.writeToken("https://pod-ip:8999/wrapped-token", wrapInfo);
  }
}

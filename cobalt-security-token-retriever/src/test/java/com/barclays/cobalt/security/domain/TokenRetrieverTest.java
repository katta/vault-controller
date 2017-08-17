package com.barclays.cobalt.security.domain;

import org.assertj.core.util.Files;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import static com.barclays.cobalt.security.domain.TokenRetriever.VAULT_TOKEN_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(TokenRetriever.class)
public class TokenRetrieverTest {

  private static final URI UNWRAP_ENDPOINT = URI.create("http://localhost:8200/v1/sys/wrapping/unwrap");
  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private RestTemplateBuilder builder;

  @Value("classpath:token-response.json")
  private Resource body;

  private TokenRetriever tokenRetriever;
  private File tokenFile;

  @Before
  public void setUp() throws Exception {
    tokenFile = Files.newTemporaryFile();
    tokenRetriever = new TokenRetriever(builder, UNWRAP_ENDPOINT, Paths.get(tokenFile.toURI()));
  }

  @Test
  public void shouldRetrieveWrappedToken() throws Exception {

    String wrappedToken = "wrapped-token";

    server.expect(requestTo(UNWRAP_ENDPOINT))
        .andExpect(MockRestRequestMatchers.header(VAULT_TOKEN_HEADER, wrappedToken))
        .andRespond(withSuccess(body, MediaType.APPLICATION_JSON_UTF8));

    tokenRetriever.retrieveAndSave(wrappedToken);

    assertThat(Files.contentOf(tokenFile, Charset.defaultCharset())).isEqualTo("client-token-value");

  }
}
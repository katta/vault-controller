package com.barclays.cobalt.security.tokenretriever.domain;

import com.barclays.cobalt.security.tokenretriever.service.TokenService;
import com.barclays.cobalt.security.tokenretriever.config.ApplicationProperties;
import com.barclays.cobalt.security.tokenretriever.service.ShutdownService;
import org.assertj.core.util.Files;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(TokenService.class)
public class TokenServiceTest {

  private static final URI UNWRAP_ENDPOINT = URI.create("http://localhost:8200/v1/sys/wrapping/unwrap");
  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private RestTemplateBuilder builder;

  @Value("classpath:token-response.json")
  private Resource body;

  @MockBean(answer = RETURNS_DEEP_STUBS)
  private ApplicationProperties properties;

  @MockBean
  private ShutdownService shutdown;

  private TokenService tokenRetriever;
  private File tokenFile;

  @Before
  public void setUp() throws Exception {
    tokenFile = Files.newTemporaryFile();
    given(properties.tokenFilePath()).willReturn(Paths.get(tokenFile.toURI()));
    given(properties.getVault().unwrapEndpoint()).willReturn(UNWRAP_ENDPOINT);
    tokenRetriever = new TokenService(builder.build(), shutdown, properties);
  }

  @Test
  public void shouldRetrieveWrappedToken() throws Exception {

    String wrappedToken = "wrapped-token";

    server.expect(requestTo(UNWRAP_ENDPOINT))
        .andExpect(MockRestRequestMatchers.header(TokenService.VAULT_TOKEN_HEADER, wrappedToken))
        .andRespond(withSuccess(body, MediaType.APPLICATION_JSON_UTF8));

    tokenRetriever.retrieveAndSave(wrappedToken);

    assertThat(Files.contentOf(tokenFile, Charset.defaultCharset())).isEqualTo("client-token-value");
    verify(shutdown).normally();
  }
}
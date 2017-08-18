package com.barclays.cobalt.security.tokenretriever;

import com.barclays.cobalt.security.tokenretriever.config.ApplicationProperties;
import com.barclays.cobalt.security.tokenretriever.service.ShutdownService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RunWith(MockitoJUnitRunner.class)
public class RetrieveTokenTaskTest {

  @Mock
  private ShutdownService shutdown;

  @Mock
  private ApplicationProperties properties;

  @Mock
  private RestTemplate http;

  @Test
  public void shouldInitiateNormalShutdownIfTokenCreationIsInitiatedSuccessfully() throws Exception {
    when(http.postForEntity(anyString(), anyObject(), same(Object.class))).thenReturn(new ResponseEntity<>(ACCEPTED));
    when(properties.getCallbackTimeout()).thenReturn(60);
    RetrieveTokenTask task = new RetrieveTokenTask(http, properties, shutdown);

    task.run();

    verify(shutdown).initiateDelayedShutdown();
  }

  @Test
  public void shouldShutdownWithErrorIfTokenCreationIsNotInitiatedSuccessfully() throws Exception {
    when(http.postForEntity(anyString(), anyObject(), same(Object.class))).thenReturn(new ResponseEntity<>(INTERNAL_SERVER_ERROR));
    RetrieveTokenTask task = new RetrieveTokenTask(http, properties, shutdown);

    task.run();

    verify(shutdown).abnormally();
  }
}
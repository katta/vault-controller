package com.barclays.cobalt.security.vaultcontroller.service;

import com.barclays.cobalt.security.vaultcontroller.domain.PodMetadata;
import com.barclays.cobalt.security.vaultcontroller.domain.VaultWrappedResponse;
import com.barclays.cobalt.vaultcontroller.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

  private TokenService tokenService;
  @Mock
  private TokenGenerator tokenGenerator;
  @Mock
  private OpenshiftClient openshiftClient;
  @Mock
  private PodCallbackClient podCallbackClient;

  private VaultWrappedResponse wrappedResponse;

  @Before
  public void setup() {
    tokenService = new TokenService(tokenGenerator, openshiftClient, podCallbackClient);
    wrappedResponse = new VaultWrappedResponse(new VaultWrappedResponse.WrapInfo("token", "accessor", ""));
  }

  @Test
  public void shouldGenerateVaultTokenAndSendItToInitContainer() {
    String namespace = "namespace";
    String podName = "pod-name";
    PodMetadata podMetadata = new PodMetadata(podName, namespace, Sets.newSet("pod-policy"), "callback-uri");

    when(openshiftClient.findPod(namespace, podName)).thenReturn(podMetadata);
    when(tokenGenerator.generateToken(namespace, podName, podMetadata.getPolicies())).thenReturn(wrappedResponse);

    tokenService.generateTokenForPod(namespace, podName);

    verify(openshiftClient).findPod(namespace, podName);
    verify(tokenGenerator).generateToken(namespace, podName, podMetadata.getPolicies());
    verify(podCallbackClient).writeToken(anyString(), eq(wrappedResponse.getWrapInfo()));
  }
}
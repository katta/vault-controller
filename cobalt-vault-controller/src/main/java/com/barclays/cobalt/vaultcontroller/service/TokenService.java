package com.barclays.cobalt.vaultcontroller.service;

import com.barclays.cobalt.vaultcontroller.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public class TokenService {
  private final TokenGenerator tokenGenerator;
  private final OpenshiftClient openshiftClient;
  private final PodCallbackClient podCallbackClient;

  public static final Logger logger = LoggerFactory.getLogger(TokenService.class);

  public TokenService(TokenGenerator tokenGenerator, OpenshiftClient openshiftClient, PodCallbackClient podCallbackClient) {

    this.tokenGenerator = tokenGenerator;
    this.openshiftClient = openshiftClient;
    this.podCallbackClient = podCallbackClient;
  }

  @Async
  public void generateTokenForPod(String namespace, String podName) {
    logger.info("Querying for pod with name '{}' in namespace '{}'", podName, namespace);
    PodMetadata pod = openshiftClient.findPod(namespace, podName);

    logger.info("Generating token for pod '{}' with policies '{}'", pod.getPodName(), pod.getPolicies());
    VaultWrappedResponse response = tokenGenerator.generateToken(namespace, podName, pod.getPolicies());

    logger.info("Invoking callback to init-container in pod '{}' at '{}'", pod.getPodName(), pod.getCallbackUri());
    podCallbackClient.writeToken(pod.getCallbackUri(), response.getWrapInfo());

    logger.info("Successfully invoked callback uri at '{}' for pod", pod.getCallbackUri(), pod.getPodName());
  }
}

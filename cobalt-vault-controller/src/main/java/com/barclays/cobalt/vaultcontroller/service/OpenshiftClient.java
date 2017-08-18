package com.barclays.cobalt.vaultcontroller.service;

import com.barclays.cobalt.vaultcontroller.config.ApplicationProperties;
import com.barclays.cobalt.vaultcontroller.config.ApplicationProperties.OpenshiftProperties;
import com.barclays.cobalt.vaultcontroller.domain.PodMetadata;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.model.IPod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.openshift.restclient.ResourceKind.POD;

public class OpenshiftClient {

  private final IClient client;
  private ApplicationProperties properties;

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenshiftClient.class);

  public OpenshiftClient(final ApplicationProperties properties) {
    client = new ClientBuilder(properties.getOpenshift().baseUrl())
        .sslCertCallbackWithDefaultHostnameVerifier(properties.getOpenshift().isVerifySslHostName())
        .usingToken(properties.getOpenshift().getAuthToken())
        .build();
    this.properties = properties;
  }

  public PodMetadata findPod(final String namespace, final String podName) {
    IPod pod = client.get(POD, podName, namespace);
    LOGGER.info("Retrieved metadata for pod: {} in namespace: {}", podName, namespace);
    return PodMetadata.from(pod, properties.getInitContainerPort());
  }
}

package com.barclays.cobalt.security.vaultcontroller.service;

import com.barclays.cobalt.security.vaultcontroller.config.ApplicationProperties;
import com.barclays.cobalt.security.vaultcontroller.domain.PodMetadata;
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
    client = new ClientBuilder(properties.getOpenshift().baseUri())
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

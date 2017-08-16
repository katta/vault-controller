package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.barclays.cobalt.vault.vaultcontroller.config.OpenshiftProperties;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.model.IPod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.openshift.restclient.ResourceKind.POD;

public class OpenshiftClient {

  private final IClient client;
  private OpenshiftProperties properties;

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenshiftClient.class);

  public OpenshiftClient(final OpenshiftProperties properties) {
    client = new ClientBuilder(properties.baseUrl())
        .sslCertCallbackWithDefaultHostnameVerifier(properties.isVerifySslHostName())
        .usingToken(properties.getAuthToken())
        .build();
    this.properties = properties;
  }

  public PodMetadata findPod(final String namespace, final String podName) {
    IPod pod = client.get(POD, podName, namespace);
    LOGGER.info("Retrieved metadata for pod: {} in namespace: {}", podName, namespace);
    return PodMetadata.from(pod, properties.getInitContainerPort());
  }
}

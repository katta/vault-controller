package com.barclays.cobalt.security.vaultcontroller.domain;

import com.openshift.restclient.model.IPod;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

@Data
@AllArgsConstructor
public class PodMetadata {
  public static final String VAULT_CONTROLLER_POLICY_KEY = "vaultproject.io/policies";
  public static final String INIT_PORT_NAME = "init-port";
  private final String podName;
  private final String namespace;
  private final Set<String> policies;
  private final String callbackUri;

  public static PodMetadata from(IPod pod, int initPort) {
    return new PodMetadata(pod.getName(), pod.getNamespace(), parsePolicies(pod), parseCallbackUri(pod, initPort));
  }

  private static String parseCallbackUri(IPod pod, int initPort) {
    return UriComponentsBuilder.newInstance()
        .host(pod.getIP())
        .port(initPort)
        .scheme("http")
        .path("/token")
        .build().toUriString();
  }

  private static Set<String> parsePolicies(IPod pod) {
    String annotation = pod.getAnnotation(VAULT_CONTROLLER_POLICY_KEY);
    Assert.hasLength(annotation, "Need a non-blank policy to be set." +
        " Check your deployment manifest to make sure you have set " + VAULT_CONTROLLER_POLICY_KEY);

    return stream(annotation.split("\\s*,\\s*")).collect(toSet());
  }
}

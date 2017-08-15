package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.openshift.restclient.model.IPod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PodMetadata {
  private String podName;
  private String namespace;
  private Set<String> policies;
  private String ip;

  public static PodMetadata from(IPod pod) {
    Set<String> policies = stream(pod.getAnnotation("vaultproject.io/policies").split(",")).collect(toSet());

    return new PodMetadata(pod.getName(), pod.getNamespace(), policies, pod.getIP());
  }
}

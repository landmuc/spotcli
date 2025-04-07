package com.landmuc.spotcli.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.landmuc.spotcli.model.DuckResponse;

import reactor.core.publisher.Mono;

@Component
public class DuckApiClient {

  private final WebClient duckApi;

  @Autowired
  DuckApiClient(@Qualifier("duckWebClient") WebClient duckApi) {
    this.duckApi = duckApi;
  }

  public Mono<DuckResponse> getDuck() {
    return duckApi.get()
        .uri("/quack")
        .retrieve()
        .bodyToMono(DuckResponse.class);
  }

}

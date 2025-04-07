package com.landmuc.spotcli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient spotifyWebClient(WebClient.Builder builder) {
    return builder.baseUrl("https://api.spotify.com")
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        // Authorization header will be added per request with the access token
        .build();
  }

}

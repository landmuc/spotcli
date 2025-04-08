package com.landmuc.spotcli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SpotifyClientConfig {

  @Bean
  public WebClient spotifyWebClient() {
    return WebClient.create();
  }

}

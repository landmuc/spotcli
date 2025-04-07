package com.landmuc.spotcli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DuckClientConfig {

  @Bean
  public WebClient duckWebClient() {
    return WebClient.create("https://random-d.uk/api/v2");
  }

}

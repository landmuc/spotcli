package com.landmuc.spotcli.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.landmuc.spotcli.model.UserProfileResponse;

import reactor.core.publisher.Mono;

// WebClient Wrapper for Spotify
@Component
public class SpotifyApiClient {

  private final WebClient spotifyApi;

  @Autowired
  SpotifyApiClient(@Qualifier("spotifyWebClient") WebClient spotifyApi) {
    this.spotifyApi = spotifyApi;
  }

  public Mono<UserProfileResponse> getCurrentUserProfile() {
    return spotifyApi
        .get()
        .uri("/v1/me")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(UserProfileResponse.class);
  }

}

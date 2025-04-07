package com.landmuc.spotcli.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.landmuc.spotcli.model.SpotifyBearerToken;
import com.landmuc.spotcli.model.UserProfileResponse;

import reactor.core.publisher.Mono;

// WebClient Wrapper for Spotify
@Component
@PropertySource("classpath:application-local.properties")
public class SpotifyApiClient {

  @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
  private String clientSecret;

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

  public Mono<SpotifyBearerToken> getBearerToken() {
    return spotifyApi
        .post()
        .uri("https://accounts.spotify.com/api/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED) // sets the content type to
                                                            // application/x-www-form-urlencoded
        .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
        .bodyValue("grant_type=client_credentials")
        .retrieve()
        .bodyToMono(SpotifyBearerToken.class);
  }

}

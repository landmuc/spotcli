package com.landmuc.spotcli.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.BearerTokenResponse;
import com.landmuc.spotcli.model.UserProfileResponse;
import com.landmuc.spotcli.service.AccessTokenService;

import reactor.core.publisher.Mono;

@Component
@PropertySource("classpath:application-local.properties")
public class SpotifyApiClient {

  @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.client.registration.spotify.device-id}")
  private String deviceId;

  private final WebClient spotifyWebClient;
  private final AccessTokenService accessTokenService;

  @Autowired
  SpotifyApiClient(
      @Qualifier("spotifyWebClient") WebClient spotifyWebClient,
      AccessTokenService accessTokenService) {
    this.spotifyWebClient = spotifyWebClient;
    this.accessTokenService = accessTokenService;
  }

  public Mono<BearerTokenResponse> getBearerToken() {
    return spotifyWebClient.post()
        .uri("https://accounts.spotify.com/api/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED) // sets the content type to
                                                            // application/x-www-form-urlencoded
        .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
        .bodyValue("grant_type=client_credentials")
        .retrieve()
        .bodyToMono(BearerTokenResponse.class);
  }

  public Mono<ArtistResponse> getArtistById(String artistId) {
    return getBearerToken()
        .flatMap(token -> spotifyWebClient.get()
            .uri("https://api.spotify.com/v1/artists/{id}", artistId)
            .headers(headers -> headers.setBearerAuth(token.access_token()))
            .retrieve()
            .bodyToMono(ArtistResponse.class));
  }

  public Mono<UserProfileResponse> getCurrentUserInformation() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(UserProfileResponse.class);
  }

  public Mono<String> getAvailableDevices() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player/devices")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(String.class);
  }

  public Mono<String> getPlaybackState() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(String.class);
  }

  public Mono<String> getCurrentlyPlayingTrack() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player/currently-playing")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(String.class);
  }

  public void pauseCurrentlyPlayingTrack() {
    spotifyWebClient.put()
        .uri(uriBuilder -> uriBuilder
            .scheme("https")
            .host("api.spotify.com")
            .path("/v1/me/player/pause")
            .queryParam("device_id", deviceId)
            .build())
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

  public void getNextTrack() {
    spotifyWebClient.post()
        .uri(uriBuilder -> uriBuilder
            .scheme("https")
            .host("api.spotify.com")
            .path("/v1/me/player/next")
            .queryParam("device_id", deviceId)
            .build())
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

}

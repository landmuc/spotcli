package com.landmuc.spotcli.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.CurrentlyPlayingTrackResponse;
import com.landmuc.spotcli.model.UserProfileResponse;
import com.landmuc.spotcli.service.AccessTokenService;
import com.landmuc.spotcli.service.DeviceIdService;
import com.landmuc.spotcli.service.SpotifyAuthService;

import reactor.core.publisher.Mono;

@Component
@PropertySource("classpath:application-local.properties")
public class SpotifyApiClient {

  @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
  private String clientSecret;

  private final WebClient spotifyWebClient;
  private final AccessTokenService accessTokenService;
  private final DeviceIdService deviceIdService;
  private final SpotifyAuthService spotifyAuthService;

  @Autowired
  SpotifyApiClient(
      @Qualifier("spotifyWebClient") WebClient spotifyWebClient,
      AccessTokenService accessTokenService,
      DeviceIdService deviceIdService,
      SpotifyAuthService spotifyAuthService) {
    this.spotifyWebClient = spotifyWebClient;
    this.accessTokenService = accessTokenService;
    this.deviceIdService = deviceIdService;
    this.spotifyAuthService = spotifyAuthService;
  }

  public Mono<ArtistResponse> getArtistById(String artistId) {
    return spotifyAuthService.getBearerToken()
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

  public Mono<String> getPlaybackState() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(String.class);
  }

  public Mono<CurrentlyPlayingTrackResponse> getCurrentlyPlayingTrack() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player/currently-playing")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(CurrentlyPlayingTrackResponse.class);
  }

  public void pauseCurrentlyPlayingTrack() {
    spotifyWebClient.put()
        .uri(uriBuilder -> uriBuilder
            .scheme("https")
            .host("api.spotify.com")
            .path("/v1/me/player/pause")
            .queryParam("device_id", deviceIdService.getDeviceId())
            .build())
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

  public void resumeCurrentTrack() {
    spotifyWebClient.put()
        .uri(uriBuilder -> uriBuilder
            .scheme("https")
            .host("api.spotify.com")
            .path("/v1/me/player/play")
            .queryParam("device_id", deviceIdService.getDeviceId())
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
            .queryParam("device_id", deviceIdService.getDeviceId())
            .build())
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

  public void getPreviousTrack() {
    spotifyWebClient.post()
        .uri(uriBuilder -> uriBuilder
            .scheme("https")
            .host("api.spotify.com")
            .path("/v1/me/player/previous")
            .queryParam("device_id", deviceIdService.getDeviceId())
            .build())
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

  public void setPlaybackVolume(int volumePercent) {
    spotifyWebClient.put()
        .uri(uriBuilder -> uriBuilder
            .scheme("https")
            .host("api.spotify.com")
            .path("/v1/me/player/volume")
            .queryParam("device_id", deviceIdService.getDeviceId())
            .queryParam("volume_percent", volumePercent)
            .build())
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

}

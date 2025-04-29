package com.landmuc.spotcli.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.landmuc.spotcli.domain.NewPlaybackKeyword;
import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.CurrentlyPlayingTrackResponse;
import com.landmuc.spotcli.model.PlaybackStateResponse;
import com.landmuc.spotcli.model.UserProfileResponse;
import com.landmuc.spotcli.model.UsersQueueResponse;
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

  public Mono<PlaybackStateResponse> getPlaybackState() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(PlaybackStateResponse.class);
  }

  public Mono<CurrentlyPlayingTrackResponse> getCurrentlyPlayingTrack() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player/currently-playing")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(CurrentlyPlayingTrackResponse.class);
  }

  public Mono<UsersQueueResponse> getUsersQueue() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player/queue")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(UsersQueueResponse.class);
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
        .block();
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
        .block();
  }

  public void startNewPlayback(String keyword, String[] idArray) {
    try {
      NewPlaybackKeyword newPlaybackKeyword;
      String firstElement = idArray[0];

      // should handle else case with a error message I but leave it like this for now
      if (keyword.toLowerCase().trim().equals("track")) {
        newPlaybackKeyword = NewPlaybackKeyword.TRACK;
      } else if (keyword.toLowerCase().trim().equals("album")) {
        newPlaybackKeyword = NewPlaybackKeyword.ALBUM;
      } else if (keyword.toLowerCase().trim().equals("artist")) {
        newPlaybackKeyword = NewPlaybackKeyword.ARTIST;
      } else if (keyword.toLowerCase().trim().equals("playlist")) {
        newPlaybackKeyword = NewPlaybackKeyword.PLAYLIST;
      } else {
        newPlaybackKeyword = NewPlaybackKeyword.TRACK;
      }

      Map<String, String> singleStringMap = new HashMap<>();
      Map<String, String[]> arrayMap = new HashMap<>();
      if (newPlaybackKeyword.equals(NewPlaybackKeyword.TRACK)) {
        String[] urisArray = new String[idArray.length];
        int iter = 0;
        for (String id : idArray) {
          urisArray[iter] = String.format("spotify:track:%s", id);
          iter++;
        }
        arrayMap.put("uris", urisArray);
      } else if (newPlaybackKeyword.equals(NewPlaybackKeyword.ALBUM)) {
        singleStringMap.put("context_uri", String.format("spotify:album:%s", firstElement));
      } else if (newPlaybackKeyword.equals(NewPlaybackKeyword.ARTIST)) {
        singleStringMap.put("context_uri", String.format("spotify:artist:%s", firstElement));
      } else if (newPlaybackKeyword.equals(NewPlaybackKeyword.PLAYLIST)) {
        singleStringMap.put("context_uri", String.format("spotify:playlist:%s", firstElement));
      }

      spotifyWebClient.put()
          .uri(uriBuilder -> uriBuilder
              .scheme("https")
              .host("api.spotify.com")
              .path("/v1/me/player/play")
              .queryParam("device_id", deviceIdService.getDeviceId())
              .build())
          .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
          .bodyValue((newPlaybackKeyword.equals(NewPlaybackKeyword.TRACK) ? arrayMap : singleStringMap))
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (WebClientResponseException.BadRequest e) {
      System.err.println("Invalid id provided: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Oh no! An error happend: " + e.getMessage());
    }
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
        .block();
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
        .block();
  }

  public void setPlaybackVolume(int volumePercent) {
    try {

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
          .block();
    } catch (NumberFormatException e) {
      System.err.println("Invalid volume value! Use a number between 0 - 100! " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Oh no! An error happend: " + e.getMessage());
    }
  }

  public void togglePlaybackShuffle(boolean shuffleState) {
    spotifyWebClient.put()
        .uri(uriBuilder -> uriBuilder
            .scheme("https")
            .host("api.spotify.com")
            .path("/v1/me/player/shuffle")
            .queryParam("device_id", deviceIdService.getDeviceId())
            .queryParam("state", shuffleState)
            .build())
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  // could also add episode items BUT NOT RIGHT NOW! > do it with a keyword
  public void addItemToPlaybackQueue(String itemId) {
    spotifyWebClient.post()
        .uri(uriBuilder -> uriBuilder
            .scheme("https")
            .host("api.spotify.com")
            .path("/v1/me/player/queue")
            .queryParam("device_id", deviceIdService.getDeviceId())
            .queryParam("uri", String.format("spotify:track:%s", itemId))
            .build())
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  public Mono<String> search(String[] searchType, String searchString) {
    return spotifyAuthService.getBearerToken()
        .flatMap(token -> spotifyWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host("api.spotify.com")
                .path("/v1/search")
                .queryParam("q", searchString)
                .queryParam("type", searchType)
                // .queryParam("limit", 20)
                .build())
            .headers(headers -> headers.setBearerAuth(token.access_token()))
            .retrieve()
            .bodyToMono(String.class));
  }

}

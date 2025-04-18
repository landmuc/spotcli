package com.landmuc.spotcli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.client.SpotifyApiClient;
import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.BearerTokenResponse;
import com.landmuc.spotcli.model.UserProfileResponse;

import reactor.core.publisher.Mono;

@Service
public class SpotifyService {

  private final SpotifyApiClient spotifyApiClient;

  @Autowired
  SpotifyService(SpotifyApiClient spotifyApiClient) {
    this.spotifyApiClient = spotifyApiClient;
  }

  public Mono<BearerTokenResponse> getBearerToken() {
    return spotifyApiClient.getBearerToken();
  }

  public Mono<ArtistResponse> getArtistById(String artistId) {
    return spotifyApiClient.getArtistById(artistId);
  }

  public Mono<UserProfileResponse> getCurrentUserInformation() {
    return spotifyApiClient.getCurrentUserInformation();
  }

  public void getNextTrack() {
    spotifyApiClient.getNextTrack();
  }

}

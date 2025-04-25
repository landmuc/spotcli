package com.landmuc.spotcli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.client.SpotifyApiClient;
import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.DeviceListResponse;
import com.landmuc.spotcli.model.UserProfileResponse;

import reactor.core.publisher.Mono;

@Service
public class SpotifyService {
  private final SpotifyApiClient spotifyApiClient;
  private final DeviceIdService deviceIdService;

  @Autowired
  SpotifyService(SpotifyApiClient spotifyApiClient, DeviceIdService deviceIdService) {
    this.spotifyApiClient = spotifyApiClient;
    this.deviceIdService = deviceIdService;
  }

  public Mono<ArtistResponse> getArtistById(String artistId) {
    return spotifyApiClient.getArtistById(artistId);
  }

  public Mono<UserProfileResponse> getCurrentUserInformation() {
    return spotifyApiClient.getCurrentUserInformation();
  }

  public Mono<DeviceListResponse> getAvailableDevices() {
    return spotifyApiClient.getAvailableDevices()
        .filter(deviceListResponse -> !deviceListResponse.devices().isEmpty())
        .doOnNext(deviceListResponse -> {
          deviceIdService.setDeviceId(deviceListResponse.devices().getFirst().id());
        })
        .switchIfEmpty(Mono.error(new RuntimeException("Device list is empty")));

  }

  public Mono<String> getPlaybackState() {
    return spotifyApiClient.getPlaybackState();
  }

  public Mono<String> getCurrentlyPlayingTrack() {
    return spotifyApiClient.getCurrentlyPlayingTrack();
  }

  public void pauseCurrentlyPlayingTrack() {
    spotifyApiClient.pauseCurrentlyPlayingTrack();
  }

  public void getNextTrack() {
    spotifyApiClient.getNextTrack();
  }

}

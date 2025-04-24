package com.landmuc.spotcli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.BearerTokenResponse;
import com.landmuc.spotcli.model.DeviceListResponse;
import com.landmuc.spotcli.model.UserProfileResponse;
import com.landmuc.spotcli.service.SpotifyService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

  private final SpotifyService spotifyService;

  @Autowired
  SpotifyController(SpotifyService spotifyService) {
    this.spotifyService = spotifyService;
  }

  @PostMapping("/token")
  public Mono<BearerTokenResponse> getBearerToken() {
    return spotifyService.getBearerToken();
  }

  @GetMapping("/artists/{artistId}")
  public Mono<ArtistResponse> getArtistById(@PathVariable String artistId) {
    return spotifyService.getArtistById(artistId);
  }

  @GetMapping("/user")
  public Mono<UserProfileResponse> getCurrentUserInformation() {
    return spotifyService.getCurrentUserInformation();
  }

  @GetMapping("/devices")
  public DeviceListResponse getAvailableDevices() {
    return spotifyService.getAvailableDevices();
  }

  @GetMapping("/playback")
  public Mono<String> getPlaybackState() {
    return spotifyService.getPlaybackState();
  }

  @GetMapping("/current-track")
  public Mono<String> getCurrentlyPlayingTrack() {
    return spotifyService.getCurrentlyPlayingTrack();
  }

  @GetMapping("/pause-current-track")
  public void pauseCurrentlyPlayingTrack() {
    spotifyService.pauseCurrentlyPlayingTrack();
  }

  // Should be a @PostMapping but for testing in browser needs to be @GetMapping;
  // but currently doesn't work either way
  @GetMapping("/next-track")
  public void getNextTrack() {
    spotifyService.getNextTrack();
  }

}

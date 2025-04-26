package com.landmuc.spotcli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.CurrentlyPlayingTrackResponse;
import com.landmuc.spotcli.model.UserProfileResponse;
import com.landmuc.spotcli.service.SpotifyService;

import reactor.core.publisher.Mono;

//
// Leaving it in here for now if I want to test the app via browser/curl/postman whatever
//
@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

  private final SpotifyService spotifyService;

  @Autowired
  SpotifyController(SpotifyService spotifyService) {
    this.spotifyService = spotifyService;
  }

  @GetMapping("/artists/{artistId}")
  public Mono<ArtistResponse> getArtistById(@PathVariable String artistId) {
    return spotifyService.getArtistById(artistId);
  }

  @GetMapping("/user")
  public Mono<UserProfileResponse> getCurrentUserInformation() {
    return spotifyService.getCurrentUserInformation();
  }

  @GetMapping("/playback")
  public Mono<String> getPlaybackState() {
    return spotifyService.getPlaybackState();
  }

  @GetMapping("/current-track")
  public Mono<CurrentlyPlayingTrackResponse> getCurrentlyPlayingTrack() {
    return spotifyService.getCurrentlyPlayingTrack();
  }

  @PutMapping("/pause-current-track")
  public void pauseCurrentlyPlayingTrack() {
    spotifyService.pauseCurrentlyPlayingTrack();
  }

  @PutMapping("/resume-current-track")
  public void resumeCurrentTrack() {
    spotifyService.resumeCurrentTrack();
  }

  // Should be a @PostMapping but for testing in browser needs to be @GetMapping;
  // but currently doesn't work either way
  @PostMapping("/next-track")
  public void getNextTrack() {
    spotifyService.getNextTrack();
  }

  @PostMapping("/previous-track")
  public void getPreviousTrack() {
    spotifyService.getPreviousTrack();
  }

}

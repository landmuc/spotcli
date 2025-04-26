package com.landmuc.spotcli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.client.SpotifyApiClient;
import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.CurrentlyPlayingTrackResponse;
import com.landmuc.spotcli.model.PlaybackStateResponse;
import com.landmuc.spotcli.model.UserProfileResponse;
import com.landmuc.spotcli.model.UsersQueueResponse;

import reactor.core.publisher.Mono;

@Service
public class SpotifyService {
  private final SpotifyApiClient spotifyApiClient;

  @Autowired
  SpotifyService(SpotifyApiClient spotifyApiClient) {
    this.spotifyApiClient = spotifyApiClient;
  }

  public Mono<ArtistResponse> getArtistById(String artistId) {
    return spotifyApiClient.getArtistById(artistId);
  }

  public Mono<UserProfileResponse> getCurrentUserInformation() {
    return spotifyApiClient.getCurrentUserInformation();
  }

  public Mono<PlaybackStateResponse> getPlaybackState() {
    return spotifyApiClient.getPlaybackState();
  }

  public Mono<CurrentlyPlayingTrackResponse> getCurrentlyPlayingTrack() {
    return spotifyApiClient.getCurrentlyPlayingTrack();
  }

  public Mono<UsersQueueResponse> getUsersQueue() {
    return spotifyApiClient.getUsersQueue();
  }

  public void pauseCurrentlyPlayingTrack() {
    spotifyApiClient.pauseCurrentlyPlayingTrack();
  }

  public void resumeCurrentTrack() {
    spotifyApiClient.resumeCurrentTrack();
  }

  public void getNextTrack() {
    spotifyApiClient.getNextTrack();
  }

  public void getPreviousTrack() {
    spotifyApiClient.getPreviousTrack();
  }

  public void setPlaybackVolume(int volumePercent) {
    spotifyApiClient.setPlaybackVolume(volumePercent);
  }

  public void togglePlaybackState(boolean shuffleState) {
    spotifyApiClient.togglePlaybackShuffle(shuffleState);
  }

}

package com.landmuc.spotcli.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.landmuc.spotcli.controller.SpotifyAuthController;
import com.landmuc.spotcli.controller.SpotifyController;
import com.landmuc.spotcli.domain.DelayedResponseHandler;
import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.CurrentlyPlayingTrackResponse;
import com.landmuc.spotcli.model.UserProfileResponse;

import reactor.core.publisher.Mono;

import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class SpotifyCommands {

  private final SpotifyController spotifyController;
  private final SpotifyAuthController spotifyAuthController;

  SpotifyCommands(
      SpotifyController spotifyController,
      SpotifyAuthController spotifyAuthController) {
    this.spotifyController = spotifyController;
    this.spotifyAuthController = spotifyAuthController;
  }

  @ShellMethod(key = "authorize", value = "Get Spotify authorization URL")
  public String authorize() {
    return "Please visit this URL to authorize: "
        + spotifyAuthController.getUserAuthorization().getHeaders().getLocation();
  }

  @ShellMethod(key = "artist", value = "Get artist by id")
  public ArtistResponse artist(@ShellOption("Spotify artist id") String artistId) {
    return spotifyController.getArtistById(artistId).block();
  }

  @ShellMethod(key = "user", value = "Get current user information")
  public UserProfileResponse user() {
    return spotifyController.getCurrentUserInformation().block();
  }

  @ShellMethod(key = "t", value = "Get current playing track")
  public String getCurrentlyPlayingTrack() {
    CurrentlyPlayingTrackResponse currentTrack = spotifyController.getCurrentlyPlayingTrack().block();

    return currentTrack == null ? "No track playing right now!" : "Playing: " + currentTrack.toString();
  }

  @ShellMethod(key = "p", value = "Pause current track")
  public String pauseCurrentlyPlayingTrack() {
    spotifyController.pauseCurrentlyPlayingTrack();
    CurrentlyPlayingTrackResponse currentTrack = spotifyController.getCurrentlyPlayingTrack().block();

    return currentTrack == null ? "No track playing right now!" : "Pausing: " + currentTrack.toString();

  }

  @ShellMethod(key = "s", value = "Start or resume current track")
  public String resumeCurrentTrack() {
    spotifyController.resumeCurrentTrack();
    CurrentlyPlayingTrackResponse currentTrack = spotifyController.getCurrentlyPlayingTrack().block();

    return currentTrack == null ? "No track playing right now!" : "Starting: " + currentTrack.toString();

  }

  @ShellMethod(key = "nt", value = "Play next track")
  public String nextTrack() {
    CurrentlyPlayingTrackResponse currentTrack = spotifyController.getCurrentlyPlayingTrack().block();
    spotifyController.getNextTrack();
    Mono<CurrentlyPlayingTrackResponse> nextTrackResponse = spotifyController.getCurrentlyPlayingTrack();

    CurrentlyPlayingTrackResponse nextTrack = DelayedResponseHandler
        .handleDelayedResponse(nextTrackResponse, currentTrack).block();

    return nextTrack == null ? "No track playing right now!" : "Starting: " + nextTrack.toString();
  }

  @ShellMethod(key = "pt", value = "Play previous track")
  public String previousTrack() {
    CurrentlyPlayingTrackResponse currentTrack = spotifyController.getCurrentlyPlayingTrack().block();
    spotifyController.getPreviousTrack();
    Mono<CurrentlyPlayingTrackResponse> previousTrackResponse = spotifyController.getCurrentlyPlayingTrack();

    CurrentlyPlayingTrackResponse previousTrack = DelayedResponseHandler
        .handleDelayedResponse(previousTrackResponse, currentTrack).block();

    return previousTrack == null ? "No track playing right now!" : "Starting: " + previousTrack.toString();
  }
}

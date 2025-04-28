package com.landmuc.spotcli.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.landmuc.spotcli.controller.SpotifyAuthController;
import com.landmuc.spotcli.domain.DelayedResponseHandler;
import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.CurrentlyPlayingTrackResponse;
import com.landmuc.spotcli.model.UserProfileResponse;
import com.landmuc.spotcli.model.UsersQueueResponse;
import com.landmuc.spotcli.service.SpotifyService;

import reactor.core.publisher.Mono;

import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class SpotifyCommands {

  private final SpotifyAuthController spotifyAuthController;

  private final SpotifyService spotifyService;

  public SpotifyCommands(
      SpotifyAuthController spotifyAuthController,
      SpotifyService spotifyService) {
    this.spotifyAuthController = spotifyAuthController;
    this.spotifyService = spotifyService;
  }

  @ShellMethod(key = "authorize", value = "Get Spotify authorization URL")
  public String authorize() {
    return "Please visit this URL to authorize: "
        + spotifyAuthController.getUserAuthorization().getHeaders().getLocation();
  }

  @ShellMethod(key = "artist", value = "Get artist by id")
  public ArtistResponse artist(@ShellOption("Spotify artist id") String artistId) {
    return spotifyService.getArtistById(artistId).block();
  }

  @ShellMethod(key = "user", value = "Get current user information")
  public UserProfileResponse user() {
    return spotifyService.getCurrentUserInformation().block();
  }

  @ShellMethod(key = "t", value = "Get current playing track")
  public String getCurrentlyPlayingTrack() {
    CurrentlyPlayingTrackResponse currentTrack = spotifyService.getCurrentlyPlayingTrack().block();

    return currentTrack == null ? "No track playing right now!" : "Playing: " + currentTrack.toString();
  }

  @ShellMethod(key = "q", value = "Get users track queue")
  public String getUsersQueue() {
    UsersQueueResponse usersQueueResponse = spotifyService.getUsersQueue().block();

    return usersQueueResponse.toString();
  }

  @ShellMethod(key = "p", value = "Pause current track")
  public String pauseCurrentlyPlayingTrack() {
    spotifyService.pauseCurrentlyPlayingTrack();
    CurrentlyPlayingTrackResponse currentTrack = spotifyService.getCurrentlyPlayingTrack().block();

    return currentTrack == null ? "No track playing right now!" : "Pausing: " + currentTrack.toString();

  }

  @ShellMethod(key = "s", value = "Start or resume current track")
  public String resumeCurrentTrack() {
    spotifyService.resumeCurrentTrack();
    // TODO: Why not use .block() in the actual api request?
    CurrentlyPlayingTrackResponse currentTrack = spotifyService.getCurrentlyPlayingTrack().block();

    return currentTrack == null ? "No track playing right now!" : "Starting: " + currentTrack.toString();

  }

  @ShellMethod(key = "play", value = "Play a track, a album, a artist or a playlist by providing an id")
  public String startNewPlayback(String keyword, String ids) {
    // split the ids string by '+' or ',' or '#' characters
    // to give the user some options for convenience
    String[] idArray = ids.split("[,+#]");

    // trim each id to remove any potential whitespace
    for (int i = 0; i < idArray.length; i++) {
      idArray[i] = idArray[i].trim();
    }

    spotifyService.startNewPlayback(keyword, idArray);
    return "Starts playing...";
  }

  @ShellMethod(key = "nt", value = "Play next track")
  public String nextTrack() {
    CurrentlyPlayingTrackResponse currentTrack = spotifyService.getCurrentlyPlayingTrack().block();
    spotifyService.getNextTrack();
    Mono<CurrentlyPlayingTrackResponse> nextTrackResponse = spotifyService.getCurrentlyPlayingTrack();

    CurrentlyPlayingTrackResponse nextTrack = DelayedResponseHandler
        .handleDelayedResponse(nextTrackResponse, currentTrack).block();

    return nextTrack == null ? "No track playing right now!" : "Starting: " + nextTrack.toString();
  }

  @ShellMethod(key = "pt", value = "Play previous track")
  public String previousTrack() {
    CurrentlyPlayingTrackResponse currentTrack = spotifyService.getCurrentlyPlayingTrack().block();
    spotifyService.getPreviousTrack();
    Mono<CurrentlyPlayingTrackResponse> previousTrackResponse = spotifyService.getCurrentlyPlayingTrack();

    CurrentlyPlayingTrackResponse previousTrack = DelayedResponseHandler
        .handleDelayedResponse(previousTrackResponse, currentTrack).block();

    return previousTrack == null ? "No track playing right now!" : "Starting: " + previousTrack.toString();
  }

  @ShellMethod(key = "v", value = "Set volume in percent 0 - 100%")
  public String setPlaybackVolume(int volumePercent) {

    if (volumePercent < 0 || volumePercent > 100) {
      System.err.println("Invalid volume percentage, got: " + volumePercent);
      return "Volume must be between 0 and 100!";
    }
    spotifyService.setPlaybackVolume(volumePercent);

    return String.format("Volume: %d%%", volumePercent);
  }

  @ShellMethod(key = "sh", value = "Toggle playback shuffle")
  public String togglePlaybackShuffle() {
    boolean shuffleState = !spotifyService.getPlaybackState().block().shuffle_state();

    spotifyService.togglePlaybackState(shuffleState);

    return String.format("Shuffle: %b", shuffleState);
  }
}

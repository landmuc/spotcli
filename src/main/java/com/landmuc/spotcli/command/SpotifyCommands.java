package com.landmuc.spotcli.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.landmuc.spotcli.controller.SpotifyAuthController;
import com.landmuc.spotcli.controller.SpotifyController;
import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.BearerTokenResponse;
import com.landmuc.spotcli.model.DeviceListResponse;
import com.landmuc.spotcli.model.UserProfileResponse;

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

  @ShellMethod(key = "token", value = "Get Bearer token")
  public BearerTokenResponse token() {
    return spotifyAuthController.getBearerToken().block();
  }

  @ShellMethod(key = "authorize", value = "Get Spotify authorization URL")
  public String authorize() {
    return "Please visit this URL to authorize: "
        + spotifyAuthController.getUserAuthorization().getHeaders().getLocation();
  }

  @ShellMethod(key = "devices", value = "Get available devices")
  public DeviceListResponse getAvailableDevices() {
    return spotifyAuthController.getAvailableDevices().block();
  }

  @ShellMethod(key = "artist", value = "Get artist by id")
  public ArtistResponse artist(@ShellOption("Spotify artist id") String artistId) {
    return spotifyController.getArtistById(artistId).block();
  }

  @ShellMethod(key = "user", value = "Get current user information")
  public UserProfileResponse user() {
    return spotifyController.getCurrentUserInformation().block();
  }

  @ShellMethod(key = "next_track", value = "Play next track")
  public String next_track() {
    spotifyController.getNextTrack();
    return "Playing next track...";
  }

}

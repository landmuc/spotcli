package com.landmuc.spotcli.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.landmuc.spotcli.controller.SpotifyAuthController;
import com.landmuc.spotcli.controller.SpotifyController;
import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.BearerTokenResponse;
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
    return spotifyController.getBearerToken().block();
  }

  @ShellMethod(key = "authorization", value = "Authorize on Spotify")
  public void authorization() {
    spotifyAuthController.getUserAuthorization();
  }

  @ShellMethod(key = "artist", value = "Get artist by id")
  public ArtistResponse artist(@ShellOption("Spotify artist id") String artistId) {
    return spotifyController.getArtistById(artistId).block();
  }

  @ShellMethod(key = "user", value = "Get current user information")
  public UserProfileResponse user() {
    return spotifyController.getCurrentUserInformation().block();
  }

}

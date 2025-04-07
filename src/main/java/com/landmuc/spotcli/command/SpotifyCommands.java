package com.landmuc.spotcli.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.landmuc.spotcli.controller.SpotifyController;
import com.landmuc.spotcli.model.SpotifyBearerToken;

@ShellComponent
public class SpotifyCommands {

  private final SpotifyController spotifyController;

  SpotifyCommands(SpotifyController spotifyController) {
    this.spotifyController = spotifyController;
  }

  @ShellMethod(key = "token", value = "Get Bearer Token")
  public SpotifyBearerToken token() {
    return spotifyController.getBearerToken().block();
  }

  @ShellMethod(key = "works", value = "Checks if spring shell is working")
  public String works() {
    return "Spring Shell is working!";
  }

  @ShellMethod(key = "second", value = "This is the second command")
  public String second() {
    return "This is the second command";
  }
}

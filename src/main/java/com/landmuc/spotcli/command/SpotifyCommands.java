package com.landmuc.spotcli.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class SpotifyCommands {

  @ShellMethod(key = "works", value = "Checks if spring shell is working")
  public String works() {
    return "Spring Shell is working!";
  }

  @ShellMethod(key = "second", value = "This is the second command")
  public String second() {
    return "This is the second command";
  }
}

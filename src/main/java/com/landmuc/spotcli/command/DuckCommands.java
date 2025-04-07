package com.landmuc.spotcli.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.landmuc.spotcli.controller.DuckController;
import com.landmuc.spotcli.model.DuckResponse;


@ShellComponent
public class DuckCommands {

  private final DuckController duckController;

  @Autowired
  DuckCommands(DuckController duckController) {
    this.duckController = duckController;
  }

  @ShellMethod(key = "quack", value = "Starts the quacking!")
  public DuckResponse quack() {
    return duckController.getDuck();
  }

}

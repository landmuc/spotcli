package com.landmuc.spotcli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.landmuc.spotcli.model.DuckResponse;
import com.landmuc.spotcli.service.DuckService;


@RestController
@RequestMapping("/api/duck")
public class DuckController {

  private final DuckService duckService;

  @Autowired
  DuckController(DuckService duckService) {
    this.duckService = duckService;
  }

  @GetMapping("/quack")
  public DuckResponse getDuck() {
    return duckService.getDuck();
  }

}

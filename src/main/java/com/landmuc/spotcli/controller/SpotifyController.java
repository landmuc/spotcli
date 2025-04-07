package com.landmuc.spotcli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.landmuc.spotcli.model.UserProfileResponse;
import com.landmuc.spotcli.service.SpotifyService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

  private final SpotifyService spotifyServiceImpl;

  @Autowired
  SpotifyController(SpotifyService spotifyServiceImpl) {
    this.spotifyServiceImpl = spotifyServiceImpl;
  }

  @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<UserProfileResponse> getCurrentUserProfile() {
    return spotifyServiceImpl.getCurrentUserProfile();
  }

}

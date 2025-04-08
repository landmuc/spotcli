package com.landmuc.spotcli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.landmuc.spotcli.model.ArtistResponse;
import com.landmuc.spotcli.model.SpotifyBearerToken;
import com.landmuc.spotcli.service.SpotifyService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

  private final SpotifyService spotifyService;

  @Autowired
  SpotifyController(SpotifyService spotifyService) {
    this.spotifyService = spotifyService;
  }

  @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<SpotifyBearerToken> getBearerToken() {
    return spotifyService.getBearerToken();
  }

  @GetMapping(value = "/artists/{artistId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ArtistResponse> getArtistById(@PathVariable String artistId) {
    return spotifyService.getArtistById(artistId);
  }

}

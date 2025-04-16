package com.landmuc.spotcli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.client.SpotifyApiAuthClient;

@Service
public class SpotifyAuthService {

  private SpotifyApiAuthClient spotifyApiAuthClient;

  @Autowired
  public SpotifyAuthService(SpotifyApiAuthClient spotifyApiAuthClient) {
    this.spotifyApiAuthClient = spotifyApiAuthClient;
  }

  public String getAuthorizationUrl() {
    return spotifyApiAuthClient.getAuthorizationUrl();
  }
}

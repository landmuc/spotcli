package com.landmuc.spotcli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.client.SpotifyApiAuthClient;

@Service
public class SpotifyAuthService {

  private SpotifyApiAuthClient spotifyApiAuthClient;
  private AccessTokenService accessTokenService;

  @Autowired
  public SpotifyAuthService(SpotifyApiAuthClient spotifyApiAuthClient, AccessTokenService accessTokenService) {
    this.spotifyApiAuthClient = spotifyApiAuthClient;
    this.accessTokenService = accessTokenService;
  }

  public String getAuthorizationUrl() {
    return spotifyApiAuthClient.getAuthorizationUrl();
  }

  public void getAccessToken(String code) {
    accessTokenService.setAccessTokenResponse(spotifyApiAuthClient.getAccessToken(code).block());
  }
}

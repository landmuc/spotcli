package com.landmuc.spotcli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.client.SpotifyApiAuthClient;
import com.landmuc.spotcli.model.AccessTokenResponse;

import reactor.core.publisher.Mono;

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

  public Mono<AccessTokenResponse> getAccessToken(String code) {
    return spotifyApiAuthClient.getAccessToken(code)
        // filter out null responses if they occur so the Mono is empty and gets catched
        // by .switchIfEmpty()
        .filter(accessTokenResponse -> accessTokenResponse != null)
        // doOnNext is used for side effects that occur WHEN a item is emitted.
        .doOnNext(accessTokenResponse -> {
          accessTokenService.setAccessTokenResponse(accessTokenResponse);
        })
        .switchIfEmpty(Mono.error(new RuntimeException("Access token could not be retrieved")));
  }
}

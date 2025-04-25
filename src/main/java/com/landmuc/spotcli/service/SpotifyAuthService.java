package com.landmuc.spotcli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.client.SpotifyApiAuthClient;
import com.landmuc.spotcli.model.AccessTokenResponse;
import com.landmuc.spotcli.model.BearerTokenResponse;
import com.landmuc.spotcli.model.DeviceListResponse;

import reactor.core.publisher.Mono;

@Service
public class SpotifyAuthService {

  private SpotifyApiAuthClient spotifyApiAuthClient;
  private AccessTokenService accessTokenService;
  private DeviceIdService deviceIdService;

  @Autowired
  public SpotifyAuthService(SpotifyApiAuthClient spotifyApiAuthClient,
      AccessTokenService accessTokenService,
      DeviceIdService deviceIdService) {
    this.spotifyApiAuthClient = spotifyApiAuthClient;
    this.accessTokenService = accessTokenService;
    this.deviceIdService = deviceIdService;
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

  public Mono<DeviceListResponse> getAvailableDevices() {
    return spotifyApiAuthClient.getAvailableDevices()
        .filter(deviceListResponse -> !deviceListResponse.devices().isEmpty())
        .doOnNext(deviceListResponse -> {
          deviceIdService.setDeviceId(deviceListResponse.devices().getFirst().id());
        })
        .switchIfEmpty(Mono.error(new RuntimeException("Device list is empty")));

  }

  // is not needed if authorization via access token is always used
  public Mono<BearerTokenResponse> getBearerToken() {
    return spotifyApiAuthClient.getBearerToken();
  }
}

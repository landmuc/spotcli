package com.landmuc.spotcli.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.landmuc.spotcli.model.AccessTokenResponse;

// @Scope tells Spring to use only one instance of AccessTokenService for the given session.
// So for every request we make Spring uses the same AccessTokenService instances which has our current access token
// without @Scope Spring would create a new instance of AccessTokenService for every request which would have a access token of null
// and we would not be authorized to make the request.
@Service
// @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS) // need
// spring-boot-starter-web impl for this to
@Scope("singleton") // work
public class AccessTokenService {
  private AccessTokenResponse accessTokenResponse;

  public void setAccessTokenResponse(AccessTokenResponse accessTokenResponse) {
    this.accessTokenResponse = accessTokenResponse;
  }

  public AccessTokenResponse getAccessTokenResponse() {
    return accessTokenResponse;
  }
}

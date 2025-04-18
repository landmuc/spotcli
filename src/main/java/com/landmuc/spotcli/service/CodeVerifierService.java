package com.landmuc.spotcli.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

// @Scope tells Spring to use only one instance of CodeVerifierService for the given session.
// So for every request we make Spring uses the same CodeVerifierService instances which has our current access token
// without @Scope Spring would create a new instance of CodeVerifierService for every request which would have a access token of null
// and we would not be authorized to make the request.
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CodeVerifierService {
  private String codeVerifier;

  public void setCodeVerifier(String codeVerifier) {
    this.codeVerifier = codeVerifier;
  }

  public String getCodeVerifier() {
    return codeVerifier;
  }
}

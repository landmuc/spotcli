package com.landmuc.spotcli.service;

import org.springframework.stereotype.Service;

// @Scope tells Spring to use only one instance of CodeVerifierService for the given session.
// So for every request we make Spring uses the same CodeVerifierService instances which has our current code verifier
// without @Scope Spring would create a new instance of CodeVerifierService for every request which would have a code verifier of null
// and we would not be authorized to make the request.
//TODO: does this really need to be in a session? 
@Service
// @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS) // need
// spring-boot-starter-web impl for this to
// work
public class CodeVerifierService {
  private String codeVerifier;

  public void setCodeVerifier(String codeVerifier) {
    this.codeVerifier = codeVerifier;
  }

  public String getCodeVerifier() {
    return codeVerifier;
  }
}

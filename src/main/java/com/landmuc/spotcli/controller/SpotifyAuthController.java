package com.landmuc.spotcli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.landmuc.spotcli.service.SpotifyAuthService;

@RestController
@RequestMapping("/api/auth")
public class SpotifyAuthController {

  private final SpotifyAuthService spotifyAuthService;

  @Autowired
  public SpotifyAuthController(SpotifyAuthService spotifyAuthService) {
    this.spotifyAuthService = spotifyAuthService;
  }

  // Location header + HttpsStatus.NotFound creates a redirect to the authUrl
  @GetMapping("/get-auth-url")
  public ResponseEntity<Void> getUserAuthorization() {
    String authUrl = spotifyAuthService.getAuthorizationUrl();
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(java.net.URI.create(authUrl));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  // gets called through getUserAuthorization()
  // RequestParam extracts the value "code" from the URL and assigns it to the
  // "code" parameter
  // Also call getDeviceId() to get the needed device id directly with the
  // authorization
  @GetMapping("/auth-redirect")
  public void handleAuthRedirect(@RequestParam("code") String code) {
    spotifyAuthService.getAccessToken(code).block();
    spotifyAuthService.getDeviceId().block();
  }

}

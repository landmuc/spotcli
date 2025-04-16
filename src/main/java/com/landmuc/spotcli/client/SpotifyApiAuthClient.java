package com.landmuc.spotcli.client;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.landmuc.spotcli.service.SpotifyPKCEService;

@Component
@PropertySource("classpath:application-local.properties")
public class SpotifyApiAuthClient {
  @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.client.provider.spotify.authorization-uri}")
  private String authUri;

  @Value("${spring.security.oauth2.client.registration.spotify.redirect-uri}")
  private String redirectUri;

  @Value("${spring.security.oauth2.client.registration.spotify.scope}")
  private String scope;

  private final SpotifyPKCEService spotifyPKCEService;

  @Autowired
  SpotifyApiAuthClient(SpotifyPKCEService spotifyPKCEService) {
    this.spotifyPKCEService = spotifyPKCEService;
  }

  public String getAuthorizationUrl() {
    String codeVerifier = spotifyPKCEService.generateCodeVerifier();
    String codeChallenge;

    try {
      codeChallenge = spotifyPKCEService.generateCodeChallenge(codeVerifier);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Failed to generate code challenge", e);
    }

    return UriComponentsBuilder.fromUriString(authUri)
        .queryParam("client_id", clientId)
        .queryParam("response_type", "code")
        .queryParam("redirect_uri", redirectUri)
        .queryParam("scope", scope)
        .queryParam("code_challenge_method", "S256")
        .queryParam("code_challenge", codeChallenge)
        .build()
        .toUriString();
  }

}

package com.landmuc.spotcli.client;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.landmuc.spotcli.service.SpotifyPKCEService;
import com.landmuc.spotcli.service.AccessTokenService;
import com.landmuc.spotcli.service.CodeVerifierService;

import com.landmuc.spotcli.model.AccessTokenResponse;
import com.landmuc.spotcli.model.BearerTokenResponse;
import com.landmuc.spotcli.model.DeviceListResponse;

import reactor.core.publisher.Mono;

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

  private final WebClient spotifyWebClient;
  private final SpotifyPKCEService spotifyPKCEService;
  private final CodeVerifierService codeVerifierService;
  private final AccessTokenService accessTokenService;

  @Autowired
  SpotifyApiAuthClient(@Qualifier("spotifyWebClient") WebClient spotifyWebClient,
      SpotifyPKCEService spotifyPKCEService,
      CodeVerifierService codeVerifierService,
      AccessTokenService accessTokenService) {
    this.spotifyWebClient = spotifyWebClient;
    this.spotifyPKCEService = spotifyPKCEService;
    this.codeVerifierService = codeVerifierService;
    this.accessTokenService = accessTokenService;
  }

  public Mono<AccessTokenResponse> getAccessToken(String code) {
    String codeVerifier = codeVerifierService.getCodeVerifier();
    if (codeVerifier == null) {
      return Mono.error(new IllegalStateException("No code verifier found in session"));
    }

    // MultiValueMap allows each key to be associated with multiple value
    // a normal map would be enough in this case
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("code", code);
    formData.add("redirect_uri", redirectUri); // is used for validation only, there is no actual redirection
    formData.add("client_id", clientId);
    formData.add("code_verifier", codeVerifier);

    return spotifyWebClient.post()
        .uri("https://accounts.spotify.com/api/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .bodyToMono(AccessTokenResponse.class);
  }

  public String getAuthorizationUrl() {
    String codeVerifier = spotifyPKCEService.generateCodeVerifier();
    // uses the codeVerifierService instance created in the SpotifyApiAuthClient
    // initialization and sets the newly generated code verifier to it
    codeVerifierService.setCodeVerifier(codeVerifier);

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

  public Mono<DeviceListResponse> getDeviceId() {
    return spotifyWebClient.get()
        .uri("https://api.spotify.com/v1/me/player/devices")
        .headers(headers -> headers.setBearerAuth(accessTokenService.getAccessTokenResponse().access_token()))
        .retrieve()
        .bodyToMono(DeviceListResponse.class);
  }

  public Mono<BearerTokenResponse> getBearerToken() {
    return spotifyWebClient.post()
        .uri("https://accounts.spotify.com/api/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
        .bodyValue("grant_type=client_credentials")
        .retrieve()
        .bodyToMono(BearerTokenResponse.class);
  }

}

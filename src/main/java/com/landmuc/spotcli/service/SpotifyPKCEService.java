package com.landmuc.spotcli.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class SpotifyPKCEService {

  public String generateCodeVerifier() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] codeVerifier = new byte[64];
    secureRandom.nextBytes(codeVerifier);

    return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
  }

  public String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    byte[] digest = messageDigest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));

    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }

}

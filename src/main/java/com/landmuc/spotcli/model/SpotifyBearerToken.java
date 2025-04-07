package com.landmuc.spotcli.model;

public record SpotifyBearerToken(
    String access_token,
    String token_type,
    Integer expires_in) {
}

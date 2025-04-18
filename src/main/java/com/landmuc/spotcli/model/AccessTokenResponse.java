package com.landmuc.spotcli.model;

public record AccessTokenResponse(
    String access_token,
    String token_type,
    String scope,
    int expires_in,
    String refresh_token) {
}

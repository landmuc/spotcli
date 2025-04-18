package com.landmuc.spotcli.model;

public record BearerTokenResponse(
    String access_token,
    String token_type,
    Integer expires_in) {
}

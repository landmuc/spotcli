package com.landmuc.spotcli.model;

public record UserProfileResponse(
    String id,
    String display_name,
    String email,
    String uri) {
}

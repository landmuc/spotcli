package com.landmuc.spotcli.model;

public record DeviceResponse(
    String id,
    boolean is_active,
    boolean is_private_session,
    boolean is_restricted,
    String name,
    boolean supports_volume,
    String type,
    int volume_percent) {
}

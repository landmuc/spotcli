package com.landmuc.spotcli.model;

import java.util.List;

public record ItemResponse(
    String id,
    String name,
    int track_number,
    int duration_ms,
    AlbumResponse album,
    List<ArtistResponse> artists) {
}

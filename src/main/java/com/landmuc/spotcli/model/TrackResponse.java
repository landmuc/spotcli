package com.landmuc.spotcli.model;

import java.util.List;
import java.util.stream.Collectors;

import com.landmuc.spotcli.domain.TimeConverter;

public record TrackResponse(
    String id,
    String name,
    int track_number,
    int duration_ms,
    AlbumResponse album,
    List<ArtistResponse> artists) {

  @Override
  public String toString() {
    String trackName = name();
    String albumName = album().name();
    String duration = TimeConverter.formatTime(duration_ms());

    String artistsName = artists().stream()
        .map(artist -> artist.name())
        .collect(Collectors.joining(", "));

    return String.format("%s - '%s' by %s (%s)",
        trackName, albumName, artistsName, duration);
  }

}

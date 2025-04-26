package com.landmuc.spotcli.model;

import java.util.stream.Collectors;
import java.util.Objects;

import com.landmuc.spotcli.domain.TimeConverter;

public record CurrentlyPlayingTrackResponse(
    int progress_ms,
    ItemResponse item) {

  @Override
  public String toString() {
    if (item() == null) {
      return "No track is currently playing.";
    }
    int trackNumber = item().track_number();
    String trackName = item().name();
    String albumName = item().album().name();
    String progress = TimeConverter.formatTime(progress_ms());
    String duration = TimeConverter.formatTime(item().duration_ms());

    String artistsName = item().artists().stream()
        .map(artist -> artist.name())
        .collect(Collectors.joining(", "));

    return String.format("%d. %s - '%s' by %s (%s / %s)",
        trackNumber, trackName, albumName, artistsName, progress, duration);
  }

  @Override
  public boolean equals(Object obj) {
    // this == obj checks if the two objects being compared are actually the same
    // instance in memory
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    CurrentlyPlayingTrackResponse other = (CurrentlyPlayingTrackResponse) obj;

    // If both items are null, consider them equal
    if (item == null && other.item == null)
      return true;

    // If only one item is null, they're not equal
    if (item == null || other.item == null)
      return false;

    // Compare only the IDs of the ItemResponse objects
    return Objects.equals(item.id(), other.item.id());
  }

  // also using item.id() to be consistent with .equals()
  @Override
  public int hashCode() {
    return Objects.hash(item == null ? null : item.id());
  }
}

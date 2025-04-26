package com.landmuc.spotcli.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record UsersQueueResponse(
    TrackResponse currently_playing,
    List<TrackResponse> queue) {

  @Override
  public String toString() {
    String currentTrack = currently_playing.toString();

    String currentQueueOLD = queue.stream()
        .map(track -> track.toString())
        .collect(Collectors.joining("\n "));

    String currentQueue = IntStream.range(0, queue.size())
        .mapToObj(i -> (i + 1) + ". " + queue.get(i))
        .collect(Collectors.joining("\n "));

    return String.format("Currently playing:\n    %s\nIn queue:\n %s",
        currentTrack, currentQueue);
  }

}

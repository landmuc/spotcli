package com.landmuc.spotcli.model.search;

import java.util.List;

record PlaylistSearchResponse(
    String href,
    Integer limit,
    String next,
    Integer offset,
    String previous,
    Integer total,
    List<PlaylistItem> items) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total results: %d%n", total));
        for (PlaylistItem item : items) {
            sb.append(String.format("%s - '%s' by %s: %s%n",
                item.name(),
                item.description(),
                item.owner().display_name(),
                item.id()));
        }
        return sb.toString();
    }

  // Record for items in the search response
  record PlaylistItem(
      Boolean collaborative,
      String description,
      ExternalUrls external_urls,
      String href,
      String id,
      List<Image> images,
      String name,
      Owner owner,
      String snapshot_id,
      Tracks tracks,
      String type,
      String uri) {
  }

  // Record for external URLs
  record ExternalUrls(
      String spotify) {
  }

  // Record for images
  record Image(
      String url,
      Integer height,
      Integer width) {
  }

  // Record for owner details
  record Owner(
      ExternalUrls external_urls,
      String href,
      String id,
      String type,
      String uri,
      String display_name) {
  }

  // Record for track details within the playlist
  record Tracks(
      String href,
      Integer total) {
  }
}

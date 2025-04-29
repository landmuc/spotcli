package com.landmuc.spotcli.model.search;

import java.util.List;

record AlbumSearchResponse(
    String href,
    Integer limit,
    String next,
    Integer offset,
    String previous,
    Integer total,
    List<AlbumItem> items) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total results: %d%n", total));
        for (AlbumItem item : items) {
            sb.append(String.format("%s - '%s' by %s: %s%n",
                item.name(),
                item.album_type(),
                item.artists().get(0).name(),
                item.id()));
        }
        return sb.toString();
    }

  // Record for items in the search response
  record AlbumItem(
      String album_type,
      Integer total_tracks,
      List<String> available_markets,
      ExternalUrls external_urls,
      String href,
      String id,
      List<Image> images,
      String name,
      String release_date,
      String release_date_precision,
      Restrictions restrictions,
      String type,
      String uri,
      List<Artist> artists) {
  }

  // Record for artist details
  record Artist(
      ExternalUrls external_urls,
      String href,
      String id,
      String name,
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

  // Record for restrictions
  record Restrictions(
      String reason) {
  }
}

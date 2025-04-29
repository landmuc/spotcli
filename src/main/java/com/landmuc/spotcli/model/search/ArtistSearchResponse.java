package com.landmuc.spotcli.model.search;

import java.util.List;

record ArtistSearchResponse(
    String href,
    Integer limit,
    String next,
    Integer offset,
    String previous,
    Integer total,
    List<ArtistItem> items) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total results: %d%n", total));
        for (ArtistItem item : items) {
            sb.append(String.format("%s - '%s' followers: %s: %s%n",
                item.name(),
                item.genres().isEmpty() ? "No genres" : item.genres().get(0),
                item.followers().total(),
                item.id()));
        }
        return sb.toString();
    }

  // Record for items in the search response
  record ArtistItem(
      ExternalUrls external_urls,
      Followers followers,
      List<String> genres,
      String href,
      String id,
      List<Image> images,
      String name,
      Integer popularity,
      String type,
      String uri) {
  }

  // Record for external URLs
  record ExternalUrls(
      String spotify) {
  }

  // Record for follower information
  record Followers(
      String href,
      Integer total) {
  }

  // Record for images
  record Image(
      String url,
      Integer height,
      Integer width) {
  }
}

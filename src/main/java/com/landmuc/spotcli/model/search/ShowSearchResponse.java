package com.landmuc.spotcli.model.search;

import java.util.List;

record ShowSearchResponse(
    String href,
    Integer limit,
    String next,
    Integer offset,
    String previous,
    Integer total,
    List<ShowItem> items) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total results: %d%n", total));
        for (ShowItem item : items) {
            sb.append(String.format("%s - '%s' episodes: %s: %s%n",
                item.name(),
                item.publisher(),
                item.total_episodes(),
                item.id()));
        }
        return sb.toString();
    }

  // Record for items in the search response
  record ShowItem(
      List<String> available_markets,
      List<Copyright> copyrights,
      String description,
      String html_description,
      Boolean explicit,
      ExternalUrls external_urls,
      String href,
      String id,
      List<Image> images,
      Boolean is_externally_hosted,
      List<String> languages,
      String media_type,
      String name,
      String publisher,
      String type,
      String uri,
      Integer total_episodes) {
  }

  // Record for copyright details
  record Copyright(
      String text,
      String type) {
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
}

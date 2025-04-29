package com.landmuc.spotcli.model.search;

import java.util.List;

record AudiobookSearchResponse(
    String href,
    Integer limit,
    String next,
    Integer offset,
    String previous,
    Integer total,
    List<AudiobookItem> items) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total results: %d%n", total));
        for (AudiobookItem item : items) {
            sb.append(String.format("%s - '%s' by %s: %s%n",
                item.name(),
                item.publisher(),
                item.authors().get(0).name(),
                item.id()));
        }
        return sb.toString();
    }

  // Record for items in the search response
  record AudiobookItem(
      List<Author> authors,
      List<String> available_markets,
      List<Copyright> copyrights,
      String description,
      String html_description,
      String edition,
      Boolean explicit,
      ExternalUrls external_urls,
      String href,
      String id,
      List<Image> images,
      List<String> languages,
      String media_type,
      String name,
      List<Narrator> narrators,
      String publisher,
      String type,
      String uri,
      Integer total_chapters) {
  }

  // Record for author details
  record Author(
      String name) {
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

  // Record for narrator details
  record Narrator(
      String name) {
  }
}

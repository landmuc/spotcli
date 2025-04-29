package com.landmuc.spotcli.model.search;

import java.util.List;
import java.util.Map;

record TrackSearchResponse(
    String href,
    Integer limit,
    String next,
    Integer offset,
    String previous,
    Integer total,
    List<TrackItem> items) {

  // Record for items in the search response
  record TrackItem(
      Album album,
      List<Artist> artists,
      List<String> available_markets,
      Integer disc_number,
      Integer duration_ms,
      Boolean explicit,
      ExternalIds external_ids,
      ExternalUrls external_urls,
      String href,
      String id,
      Boolean is_playable,
      Restrictions restrictions,
      String name,
      Integer popularity,
      String preview_url,
      Integer track_number,
      String type,
      String uri,
      Boolean is_local) {
  }

  // Record for album details
  record Album(
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

  // Record for external IDs
  record ExternalIds(
      String isrc,
      String ean,
      String upc) {
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

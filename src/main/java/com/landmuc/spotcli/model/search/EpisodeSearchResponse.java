package com.landmuc.spotcli.model.search;

import java.util.List;

public record EpisodeSearchResponse(
    String href,
    Integer limit,
    String next,
    Integer offset,
    String previous,
    Integer total,
    List<EpisodeItem> items) {

  // Record for items in the search response
  record EpisodeItem(
      String audio_preview_url,
      String description,
      String html_description,
      Integer duration_ms,
      Boolean explicit,
      ExternalUrls external_urls,
      String href,
      String id,
      List<Image> images,
      Boolean is_externally_hosted,
      Boolean is_playable,
      String language,
      List<String> languages,
      String name,
      String release_date,
      String release_date_precision,
      ResumePoint resume_point,
      String type,
      String uri,
      Restrictions restrictions) {
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

  // Record for resume point
  record ResumePoint(
      Boolean fully_played,
      Integer resume_position_ms) {
  }

  // Record for restrictions
  record Restrictions(
      String reason) {
  }
}

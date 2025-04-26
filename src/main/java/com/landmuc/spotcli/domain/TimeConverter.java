package com.landmuc.spotcli.domain;

public class TimeConverter {

  public static String formatTime(int milliseconds) {
    long totalSeconds = milliseconds / 1000;
    long seconds = totalSeconds % 60;
    long minutes = (totalSeconds / 60) % 60;
    long hours = totalSeconds / 3600;

    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }
}

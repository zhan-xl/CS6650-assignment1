package com.xiaolinzhan.skidataserver.models;

public class RideEntry {

  private int resortID;
  private String seasonID;
  private String dayID;
  private int skierID;

  public RideEntry(int resortID, String seasonID, String dayID, int skierID) {
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.skierID = skierID;
  }

  public int getResortID() {
    return resortID;
  }

  public String getSeasonID() {
    return seasonID;
  }

  public String getDayID() {
    return dayID;
  }

  public int getSkierID() {
    return skierID;
  }

  @Override
  public String toString() {
    return "RideEntry{" +
        "resortID=" + resortID +
        ", seasonID='" + seasonID + '\'' +
        ", dayID='" + dayID + '\'' +
        ", skierID=" + skierID +
        '}';
  }
}

package com.xiaolinzhan.SkierServerSpring.models;

public class RideEntry {

  private int resortID;
  private String seasonID;
  private String dayID;

  private int skierID;
  private int time;
  private int liftID;

  public RideEntry(RidePathParams ridePathParams, RideBodyParams rideBodyParams) {
    this.resortID = ridePathParams.getResortID();
    this.seasonID = ridePathParams.getSeasonID();
    this.dayID = ridePathParams.getDayID();
    this.skierID = ridePathParams.getSkierID();
    this.time = rideBodyParams.getTime();
    this.liftID = rideBodyParams.getLiftID();
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

  public int getTime() {
    return time;
  }

  public int getLiftID() {
    return liftID;
  }
}

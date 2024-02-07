package com.xiaolinzhan.skidataserver.models;

public class RideInfo {
  private int time;
  private int liftID;

  public RideInfo(int time, int liftID) {
    this.time = time;
    this.liftID = liftID;
  }

  public int getTime() {
    return time;
  }

  public int getLiftID() {
    return liftID;
  }

  @Override
  public String toString() {
    return "RideInfo{" +
        "time=" + time +
        ", liftID=" + liftID +
        '}';
  }
}

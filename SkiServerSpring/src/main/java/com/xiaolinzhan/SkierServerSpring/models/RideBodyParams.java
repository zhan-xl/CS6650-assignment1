package com.xiaolinzhan.SkierServerSpring.models;

public class RideBodyParams {
  private int time;
  private int liftID;

  public RideBodyParams(int time, int liftID) {
    this.time = time;
    this.liftID = liftID;
  }

  public int getTime() {
    return time;
  }

  public int getLiftID() {
    return liftID;
  }
}

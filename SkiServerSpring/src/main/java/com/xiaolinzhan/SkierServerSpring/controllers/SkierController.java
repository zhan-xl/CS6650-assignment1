package com.xiaolinzhan.SkierServerSpring.controllers;

import com.xiaolinzhan.SkierServerSpring.models.RideBodyParams;
import com.xiaolinzhan.SkierServerSpring.models.RideEntry;
import com.xiaolinzhan.SkierServerSpring.models.RidePathParams;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/skiers")
public class SkierController {

  private HashMap<RidePathParams, RideBodyParams> bufferRideEntryMap = new HashMap<>();

  @PostMapping("/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
  public ResponseEntity<?> postRide(
      @PathVariable int resortID, @PathVariable String seasonID, @PathVariable String dayID, @PathVariable int skierID,
      @RequestBody RideBodyParams rideBodyParams
      ) {
    if ((resortID == 0) || (seasonID == null) || (dayID == null) || (skierID == 0) || (rideBodyParams == null)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    RidePathParams ridePathParams = new RidePathParams(resortID, seasonID, dayID, skierID);
    if (!dataValidation(ridePathParams, rideBodyParams)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(rideBodyParams, HttpStatus.CREATED);
  }

  private boolean dataValidation(RidePathParams ridePathParams, RideBodyParams rideBodyParams) {
    if (ridePathParams.getSkierID() < 1 || ridePathParams.getSkierID() > 100000) {
      return false;
    } else if (ridePathParams.getResortID() < 1 || ridePathParams.getResortID() > 10) {
      return false;
    } else if (rideBodyParams.getLiftID() < 1 || rideBodyParams.getLiftID() > 40) {
      return false;
    } else if (!ridePathParams.getSeasonID().equals("2024")) {
      return false;
    } else if (!ridePathParams.getDayID().equals("1")) {
      return false;
    } else if (rideBodyParams.getTime() < 1 || rideBodyParams.getTime() > 360) {
      return false;
    }
    return true;
  }

}

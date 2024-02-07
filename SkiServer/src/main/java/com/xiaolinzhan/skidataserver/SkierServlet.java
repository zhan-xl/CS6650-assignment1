package com.xiaolinzhan.skidataserver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import com.xiaolinzhan.skidataserver.models.RideInfo;
import tools.GetRequestBodyService;

@WebServlet(name = "skier servlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {

  private JsonObject json = new JsonObject();
  private Gson gson = new Gson();
  private String message;

  public void init() {
    message = "";
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String urlPath = request.getPathInfo();
    Map<String, String> pathParaMap = parseUrlPath(urlPath);

    if (pathParaMap == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      message = "missing path parameter";
      json.addProperty("message", message);
      response.getWriter().write(json.toString());
      return;
    }
    GetRequestBodyService service = new GetRequestBodyService();
    String body = service.getBody(request);
    RideInfo rideInfo = gson.fromJson(body, RideInfo.class);
    if (!dataValidation(pathParaMap, rideInfo)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      message = "not valid parameters";
      json.addProperty("message", message);
      response.getWriter().write(json.toString());
      return;
    }
    response.setStatus(HttpServletResponse.SC_CREATED);
    response.getWriter().write(gson.toJson(rideInfo, rideInfo.getClass()));
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    message = "you have waited for 1 sec";
    json.addProperty("message", message);
    response.getWriter().write(json.toString());

  }

  private Map<String, String> parseUrlPath(String urlPath) {
    if (urlPath == null || urlPath.isEmpty()) {
      return null;
    }
    String[] urlParas = urlPath.split("/");
    if (urlParas.length < 8 || !urlParas[0].isEmpty() || !urlParas[2].equals("seasons")
        || !urlParas[4].equals("days") || !urlParas[6].equals("skiers")) {
      return null;
    }
    String resortID = urlParas[1];
    String seaSonsID = urlParas[3];
    String dayID = urlParas[5];
    String skierID = urlParas[7];

    return new HashMap<String, String>() {{
      put("resortID", resortID);
      put("seasonID", seaSonsID);
      put("dayID", dayID);
      put("skierID", skierID);
    }};
  }

  private boolean dataValidation(Map<String, String> pathParaMap, RideInfo rideInfo) {
    if (Integer.parseInt(pathParaMap.get("resortID")) < 1
        || Integer.parseInt(pathParaMap.get("resortID")) > 10) {
      return false;
    } else if (!pathParaMap.get("seasonID").equals("2024")) {
      return false;
    } else if (!pathParaMap.get("dayID").equals("1")) {
      return false;
    } else if (Integer.parseInt(pathParaMap.get("skierID")) < 1
        || Integer.parseInt(pathParaMap.get("skierID")) > 100000) {
      return false;
    } else if (rideInfo.getTime() < 1 || rideInfo.getTime() > 360) {
      return false;
    } else if (rideInfo.getLiftID() < 1 || rideInfo.getLiftID() > 40) {
      return false;
    }
    return true;
  }

}
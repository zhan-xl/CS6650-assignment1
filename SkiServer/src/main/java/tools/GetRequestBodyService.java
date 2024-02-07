package tools;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public class GetRequestBodyService {

  StringBuilder stringBuilder = new StringBuilder();

  public String getBody(HttpServletRequest request) throws IOException {
    BufferedReader reader = request.getReader();
    String line;
    while ((line = reader.readLine()) != null) {
      stringBuilder.append(line);
      stringBuilder.append(System.lineSeparator());
    }

    return stringBuilder.toString();
  }

}

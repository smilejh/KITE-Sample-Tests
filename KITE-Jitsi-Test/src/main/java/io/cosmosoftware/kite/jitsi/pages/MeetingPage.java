package io.cosmosoftware.kite.jitsi.pages;

import io.cosmosoftware.kite.pages.BasePage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.json.*;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cosmosoftware.kite.entities.Timeouts.HALF_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class MeetingPage extends BasePage {
  public MeetingPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
  }

  public int numberOfParticipants =
      Integer.parseInt(executeJsScript(webDriver, getNumberOfParticipantScript()).toString()) + 1;
  public long statsCollectionTime;
  public long statsCollectionInterval;

  @FindBy(id = "largeVideo")
  public WebElement mainVideo;

  @FindBy(tagName = "video")
  public List<WebElement> videos;

  @FindBy(xpath = "//*[@id=\"new-toolbox\"]/div[2]/div[3]/div[1]/div/div")
  public WebElement manyTilesVideoToggle;

  @FindBy(className = "button-group-right")
  public WebElement rightButtonContainer;

  public String getStatsScript() {
    return "return APP.conference.getStats();";
  }

  public String getNumberOfParticipantScript() {
    return "return APP.conference.getNumberOfParticipantsWithTracks();";
  }

  private JsonObject stringToJson(String s) {
    JsonReader r = Json.createReader(new StringReader(s));
    return r.readObject();
  }

  public JsonObject getPCStatOnce(WebDriver webDriver) {
    Object result = executeJsScript(webDriver, getStatsScript());
    return stringToJson(formatToJsonString(result.toString().replaceAll("=", ":")));
  }

  public JsonObject getPCStatOverTime(
      WebDriver webDriver, int durationInSeconds, int intervalInSeconds) {
    do {
      waitAround(HALF_SECOND_INTERVAL);
    } while (getPCStatOnce(webDriver).getJsonObject("bandwidth") == null);
    int count = durationInSeconds / intervalInSeconds;
    Map<String, Object> statsMap = new HashMap<>();
    for (int i = 0; i < durationInSeconds; i += intervalInSeconds) {
      JsonObject data = getPCStatOnce(webDriver);
      for (String key : data.keySet()) {
        if (i == 0) {
          statsMap.put(key, data.get(key));
        } else if (i + intervalInSeconds == durationInSeconds) {
          if (data.get(key) instanceof JsonObject) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            JsonReader reader = Json.createReader(new StringReader(statsMap.get(key).toString()));
            JsonObject json = reader.readObject();
            JsonObject innerData = data.getJsonObject(key);
            for (String innerKey : innerData.keySet()) {
              if (json.get(innerKey).toString().matches("\\d+")) {
                int sum = json.getInt(innerKey);
                sum += innerData.getInt(innerKey);
                builder.add(innerKey, sum / count);
              } else {
                continue;
              }
            }
            statsMap.put(key, builder.build());
          } else if (data.get(key) instanceof JsonArray) {
            JsonArray currentArr = (JsonArray) statsMap.get(key);
            JsonArray newArr = data.getJsonArray(key);
            for (int j = 0; j < currentArr.size(); j++) {
              JsonObjectBuilder builder = Json.createObjectBuilder();
              JsonObject currentData = currentArr.getJsonObject(j);
              JsonObject newData = currentArr.getJsonObject(j);
              for (String innerKey : newData.keySet()) {
                if (newData.get(innerKey) != null
                    && newData.get(innerKey).toString().matches("\\d+")) {
                  int sum = currentData.getInt(innerKey);
                  sum += newData.getInt(innerKey);
                  builder.add(innerKey, sum / count);
                } else {
                  continue;
                }
              }
              JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
              arrayBuilder.add(builder.build());
            }

          } else if (data.get(key).toString().matches("\\d+")) {
            statsMap.put(
                key, (Integer.parseInt(statsMap.get(key).toString()) + data.getInt(key)) / count);
          }

        } else {
          if (data.get(key) instanceof JsonObject) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            JsonReader reader = Json.createReader(new StringReader(statsMap.get(key).toString()));
            JsonObject json = reader.readObject();
            System.out.println(key + "data: " + json);
            JsonObject innerData = data.getJsonObject(key);
            for (String innerKey : innerData.keySet()) {
              if (innerData.get(innerKey).toString().matches("\\d+")) {
                int sum = json.getInt(innerKey);
                sum += innerData.getInt(innerKey);
                builder.add(innerKey, sum);
              } else {
                continue;
              }
            }
            statsMap.put(key, builder.build());
          } else if (data.get(key) instanceof JsonArray) {
            JsonArray currentArr = (JsonArray) statsMap.get(key);
            JsonArray newArr = data.getJsonArray(key);
            for (int j = 0; j < currentArr.size(); j++) {
              JsonObjectBuilder builder = Json.createObjectBuilder();
              JsonObject currentData = currentArr.getJsonObject(j);
              JsonObject newData = newArr.getJsonObject(j);
              for (String innerKey : newData.keySet()) {
                if (newData.get(innerKey).toString().matches("\\d+")) {
                  int sum = currentData.getInt(innerKey);
                  sum += newData.getInt(innerKey);
                  builder.add(innerKey, sum);
                } else {
                  continue;
                }
              }
              JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
              arrayBuilder.add(builder.build());
            }

          } else if (data.get(key).toString().matches("\\d+")) {
            statsMap.put(key, Integer.parseInt(statsMap.get(key).toString()) + data.getInt(key));
          }
        }
      }
      waitAround(intervalInSeconds * 1000);
    }
    return buildAvgStats(statsMap);
  }

  private JsonObject buildAvgStats(Map<String, Object> map) {
    JsonObjectBuilder mainBuilder = Json.createObjectBuilder();
    for (String key : map.keySet()) {
      Object data = map.get(key);
      if (data instanceof JsonObject) {
        JsonReader reader = Json.createReader(new StringReader(data.toString()));
        JsonObject json = reader.readObject();
        mainBuilder.add(key, json);
      } else if (data instanceof JsonArray) {
        mainBuilder.add(key, (JsonArray) data);
      } else if (data.toString().matches("\\d+")) {
        mainBuilder.add(key, Integer.parseInt(data.toString()));
      }
    }
    return mainBuilder.build();
  }

  public String formatToJsonString(String s) {
    String r = "";
    int start;
    int end;
    boolean needSpecialFormat = false;
    for (int i = 0; i < s.length(); i++) {
      String toBeAppended = "";
      char c = s.charAt(i);
      char nextChar = (i != s.length()-1? s.charAt(i + 1) : '\0');
      if (c == '{' || (c == ' ' && nextChar != '{' && nextChar != '[')) {
        start = i + 1;
        end = start + 1;
        while (end < s.length()) {
          if (s.charAt(end) == ':') {
            i = end - 1;
            break;
          }
          end++;
        }
        String key = s.substring(start,end);
        if (key.equals("googTimingFrameInfo")) needSpecialFormat = true;
        toBeAppended = '"' + key + '"';
      } else if (c == ':') {
        start = i + 1;
        end = start + 1;
        if (!needSpecialFormat) {
          while (end < s.length()) {
            if (s.charAt(end) == '}' || s.charAt(end) == ',') {
              i = end - 1;
              break;
            }
            end++;
          }
        }
        else{
          needSpecialFormat = false;
          while (end < s.length()) {
            if (s.charAt(end) == ' ') {
              i = end-2;
              end = end -1;
              break;
            }
            end++;
          }
        }
        String value = s.substring(start, end);
        if (!value.matches("\\d+")) {
          toBeAppended = '"' + value + '"';
        } else {
          toBeAppended = value;
        }
      }
      r += c + toBeAppended;
    }
    return r;
  }
}

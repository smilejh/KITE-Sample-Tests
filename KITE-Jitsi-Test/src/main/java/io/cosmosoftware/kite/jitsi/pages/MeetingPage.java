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

import static io.cosmosoftware.kite.stats.GetStatsUtils.getStatsOnce;
import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;

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

  private JsonObject stringToJsonObject(String s) {
    JsonReader r = Json.createReader(new StringReader(s));
    return r.readObject();
  }

  private JsonArray stringToJsonArray(String s) {
    JsonReader r = Json.createReader(new StringReader(s));
    return r.readArray();
  }

  public JsonObject getPCStatOnce(WebDriver webDriver) {
    Object result = executeJsScript(webDriver, getStatsScript());
    return stringToJsonObject(result.toString().replaceAll("=", ":"));
  }

  public String getPCStatOverTime(
      WebDriver webDriver, int durationInSeconds, int intervalInSeconds, JsonArray selectedStats)
      throws Exception {
    String statString = null;
    HashMap<String, JsonArrayBuilder> videoBweMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> ssrcMap = new HashMap<>();
    for (int timer = 0; timer < durationInSeconds; timer += intervalInSeconds) {
      statString =
          formatToJsonString(getStatsOnce("jitsi", webDriver).toString().replaceAll("=", ":"));
      JsonArray jArr = stringToJsonArray(statString).getJsonArray(0);
      for (JsonValue j : jArr) {
        JsonObject data = j.asJsonObject();
        for (String key : data.keySet()) {
          if (key.equals("type")) {
            String type = data.getString(key);
            // possible selectedStats type: VideoBwe, ssrc, localcandidate, remotecandidate,
            // googTrack, googComponent, googCandidatePair, googLibjingleSession, googCertificate
            for (JsonValue value : selectedStats) {
              String statName = value.toString();
              statName = statName.substring(1, statName.length() - 1);
              if (type.equalsIgnoreCase(statName) && type.equals("ssrc")) {
                String ssrcId = data.getString("id");
                updateHashMap(ssrcMap, ssrcId, data);
              } else if (type.equalsIgnoreCase(statName) && type.equals("VideoBwe")) {
                updateHashMap(videoBweMap, "videoBwe", data);
              }
            }
          }
        }
      }
    }
    System.out.println(avgValue(videoBweMap));
    System.out.println(avgValue(ssrcMap));
    return statString;
  }

  private HashMap<String, JsonArrayBuilder> updateHashMap(
      HashMap<String, JsonArrayBuilder> hashMap, String key, JsonObject data) {
    JsonArrayBuilder arrayBuilder =
        hashMap.get(key) == null ? Json.createArrayBuilder() : hashMap.get(key);
    arrayBuilder.add(data);
    hashMap.put(key, arrayBuilder);
    return hashMap;
  }

  private HashMap<String, JsonObject> avgValue(Map<String, JsonArrayBuilder> map) {
    HashMap<String, JsonObject> newMap = new HashMap<>();
    for (String key : map.keySet()) {
      JsonArray arr = map.get(key).build();
      JsonObjectBuilder builder = Json.createObjectBuilder();
      for (String k : arr.getJsonObject(0).keySet()) {
        int sum = 0;
        int count = 0;
        for (int i = 0; i < arr.size(); i++) {
          String value = arr.getJsonObject(i).getString(k);
          if (value.matches("\\d+") & value != null & !k.equals("timestamp")) {
            sum += Integer.parseInt(value);
            count++;
            if(i + 1 == arr.size()){
              builder.add(k, sum/count);
            }
          } else {
            builder.add(k,value);
            break;
          }
        }
      }
      newMap.put(key,builder.build());
    }

    return newMap;
  }

  public String formatToJsonString(String s) {
    String r = "";
    int start;
    int end;
    boolean needSpecialFormat = false;
    for (int i = 0; i < s.length(); i++) {
      String toBeAppended = "";
      char c = s.charAt(i);
      char nextChar = (i != s.length() - 1 ? s.charAt(i + 1) : '\0');
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
        String key = s.substring(start, end);
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
        } else {
          needSpecialFormat = false;
          while (end < s.length()) {
            if (s.charAt(end) == ' ') {
              i = end - 2;
              end = end - 1;
              break;
            }
            end++;
          }
        }
        String value = s.substring(start, end);

        toBeAppended = '"' + value + '"';
      }
      r += c + toBeAppended;
    }
    return r;
  }
}

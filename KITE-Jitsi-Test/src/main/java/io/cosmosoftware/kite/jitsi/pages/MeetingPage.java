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

  public JsonObject getPCStatOverTime(
      WebDriver webDriver, int durationInSeconds, int intervalInSeconds, JsonArray selectedStats)
      throws Exception {
    String statString;
    HashMap<String, HashMap<String, JsonArrayBuilder>> allStatsMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> videoBweMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> ssrcMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> localCandidateMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> remoteCandidateMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> googTrackMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> googComponentMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> googCandidatePairMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> googLibjingleSessionMap = new HashMap<>();
    HashMap<String, JsonArrayBuilder> googCertificateMap = new HashMap<>();
    allStatsMap.put("videoBwe", videoBweMap);
    allStatsMap.put("ssrc", ssrcMap);
    allStatsMap.put("localCandidate", localCandidateMap);
    allStatsMap.put("remoteCandidate", remoteCandidateMap);
    allStatsMap.put("googTrack", googTrackMap);
    allStatsMap.put("googComponent", googComponentMap);
    allStatsMap.put("googCandidatePair", googCandidatePairMap);
    allStatsMap.put("googLibjingleSession", googLibjingleSessionMap);
    allStatsMap.put("googCertificate", googCertificateMap);
    for (int timer = 0; timer < durationInSeconds; timer += intervalInSeconds) {
      statString =
          formatToJsonString(getStatsOnce("jitsi", webDriver).toString().replaceAll("=", ":"));
      JsonArray jArr = stringToJsonArray(statString).getJsonArray(0);
      for (JsonValue j : jArr) {
        JsonObject data = j.asJsonObject();
        for (String key : data.keySet()) {
          if (key.equals("type")) {
            String type = data.getString(key);
            for (JsonValue value : selectedStats) {
              String statName = value.toString();
              statName = statName.substring(1, statName.length() - 1);
              String mapDataKey = data.getString("id");
              if (type.equalsIgnoreCase(statName)) {
                switch (type) {
                  case "VideoBwe":
                    updateHashMap(videoBweMap, "videoBwe", data);
                  case "ssrc":
                    updateHashMap(ssrcMap, mapDataKey, data);
                    break;
                  case "localcandidate":
                    updateHashMap(localCandidateMap, mapDataKey, data);
                    break;
                  case "remotecandidate":
                    updateHashMap(remoteCandidateMap, mapDataKey, data);
                    break;
                  case "googTrack":
                    updateHashMap(googTrackMap, mapDataKey, data);
                    break;
                  case "googComponent":
                    updateHashMap(googComponentMap, mapDataKey, data);
                    break;
                  case "googCandidatePair":
                    updateHashMap(googCandidatePairMap, mapDataKey, data);
                    break;
                  case "googLibjingleSession":
                    updateHashMap(googLibjingleSessionMap, mapDataKey, data);
                    break;
                  case "googCertificate":
                    updateHashMap(googCertificateMap, mapDataKey, data);
                    break;
                }
              }
            }
          }
        }
      }
    }
    return buildFinalStats(allStatsMap);
  }

  private void printMap(HashMap<String, HashMap<String, JsonArrayBuilder>> map) {
    for (String a : map.keySet()) {
      HashMap<String, JsonArrayBuilder> innerMap = map.get(a);
      if (innerMap != null) {
        for (String k : innerMap.keySet()) {
          System.out.println(k + ": " + innerMap.get(k).build());
        }
      }
    }
  }

  private JsonObject buildFinalStats(
      HashMap<String, HashMap<String, JsonArrayBuilder>> allStatsMap) {
    JsonObjectBuilder finalBuilder = Json.createObjectBuilder();
    for (String mapKey : allStatsMap.keySet()) {
      HashMap<String, JsonArrayBuilder> map = allStatsMap.get(mapKey);
      JsonArrayBuilder innerBuilder = Json.createArrayBuilder();
      JsonObject json = null;
      if (!map.isEmpty()) {
        HashMap<String, JsonObject> avgMap = avgValue(map);
        for (String key : avgMap.keySet()) {
          innerBuilder.add(avgMap.get(key));
        }
        finalBuilder.add(mapKey, innerBuilder.build());
      }
    }
    return finalBuilder.build();
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
      long startTime = 0;
      long endTime;
      for (String k : arr.getJsonObject(0).keySet()) {
        long sum = 0;
        int count = 0;
        for (int i = 0; i < arr.size(); i++) {
          String value = arr.getJsonObject(i).getString(k);
          if (value.matches("\\d+") && value != null && !k.equals("timestamp")) {
            if ((k.contains("Received") || k.contains("Sent")) && !k.contains("Rate")) {
              sum = Long.parseLong(value);
              if (i == arr.size() - 1) {
                builder.add(k, Long.toString(sum));
              }

            } else {
              sum += Long.parseLong(value);
              count++;
              if (i == arr.size() - 1) {
                builder.add(k, Long.toString(sum / count));
              }
            }
          } else if (k.equals("timestamp")) {
            if (i == 0) {
              startTime = Long.parseLong(value);
              builder.add("startTime", value);
            } else if (i == arr.size() - 1) {
              endTime = Long.parseLong(value);
              builder.add("endTime", value);
              builder.add("elapsedTime", Long.toString(endTime - startTime));
            }
          } else {
            if (i == arr.size() - 1) {
              builder.add(k, value);
            }
          }
        }
      }
      JsonObject avgStatObject = builder.build();
      newMap.put(key, avgStatObject);
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

  private JsonObject stringToJsonObject(String s) {
    JsonReader r = Json.createReader(new StringReader(s));
    return r.readObject();
  }

  private JsonArray stringToJsonArray(String s) {
    JsonReader r = Json.createReader(new StringReader(s));
    return r.readArray();
  }
}

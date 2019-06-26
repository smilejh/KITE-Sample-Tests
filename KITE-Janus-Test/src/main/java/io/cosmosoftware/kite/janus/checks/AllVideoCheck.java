package io.cosmosoftware.kite.janus.checks;

import io.cosmosoftware.kite.action.JSActionScript;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class AllVideoCheck extends TestStep {

   
  private final int numberOfParticipants;
  private final JanusPage janusPage;


  public AllVideoCheck(Runner runner, int numberOfParticipants, JanusPage janusPage) {
    super(runner);
    this.numberOfParticipants = numberOfParticipants;
    this.janusPage = janusPage;
  }

  @Override
  public String stepDescription() {
    return "Check the other videos are being received OK";
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      waitAround(3*ONE_SECOND_INTERVAL);
      //wait a while to allow all videos to load.;
      janusPage.setUserIndexList();
      List<String> remoteIdList = janusPage.getRemoteVideoIdList();
      
      List<WebElement> videos = janusPage.getTestUsersVideos();

      boolean flag = true;
      for (String videoId : remoteIdList) {
        String v2 = videoCheck(webDriver, videoId);
        flag = flag && "video".equalsIgnoreCase(v2);
      }

      int waitingTime = 0;
      while((videos.size() < remoteIdList.size() || !flag) && waitingTime < 10*numberOfParticipants) {
        flag = true;
        waitAround(ONE_SECOND_INTERVAL);
        videos = janusPage.getTestUsersVideos();
        for (String videoId : remoteIdList) {
          String v2 = videoCheck(webDriver, videoId);
          flag = flag && "video".equalsIgnoreCase(v2);
        }
        waitingTime += 1;
      }
      if (videos.size() < remoteIdList.size()) {
        throw new KiteTestException(
            "Unable to find " + remoteIdList.size() + " <video> element on the page. No video found = "
              + videos.size(), Status.FAILED);
      }
      String videoCheck = "";
      boolean error = false;
      int i = 0;
      for (String videoId : remoteIdList) {
        String v = videoCheck(webDriver, videoId);
        videoCheck += v;
        if (i < (remoteIdList.size() - 1)) {
          videoCheck += "|";
        }
        if (!"video".equalsIgnoreCase(v)) {
          error = true;
        }
        i = +1;
      }
      if (error) {
        reporter.textAttachment(report, "Received Videos", videoCheck, "plain");
        throw new KiteTestException("Some videos are still or blank: " + videoCheck, Status.FAILED);
      }
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }

  public static String videoCheck(WebDriver webDriver, String id) throws KiteTestException {
    return videoCheckSum(webDriver, id).getString("result");
  }

  public static JsonObject videoCheckSum(WebDriver webDriver, String id) throws KiteTestException {
    String result = "blank";
    JsonObjectBuilder resultObject = Json.createObjectBuilder();
    long canvasData1 = (Long)executeJsScript(webDriver, JSActionScript.getVideoFrameValueSumByIdScript(id));
    waitAround(500);
    long canvasData2 = (Long)executeJsScript(webDriver, JSActionScript.getVideoFrameValueSumByIdScript(id));
    long canvasData3 = 0L;
    if (canvasData1 != 0L || canvasData2 != 0L) {
      long diff = Math.abs(canvasData2 - canvasData1);
      if (diff != 0L) {
        result = "video";
      } else {
        waitAround(1000);
        canvasData3 = (Long)executeJsScript(webDriver, JSActionScript.getVideoFrameValueSumByIdScript(id));
        result = Math.abs(canvasData3 - canvasData1) != 0L ? "video" : "still";
      }
    }

    resultObject.add("checksum1", canvasData1).add("checksum2", canvasData2).add("canvasData3", canvasData3).add("result", result);
    return resultObject.build();
  }
}

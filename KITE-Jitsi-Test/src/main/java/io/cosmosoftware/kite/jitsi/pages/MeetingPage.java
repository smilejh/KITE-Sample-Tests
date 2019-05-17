package io.cosmosoftware.kite.jitsi.pages;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.pages.BasePage;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MeetingPage extends BasePage {

  @FindBy(id = "largeVideo")
  private WebElement mainVideo;

  @FindBy(tagName = "video")
  private List<WebElement> videos;

  @FindBy(xpath = "//*[@id=\"new-toolbox\"]/div[2]/div[3]/div[1]/div/div")
  private WebElement manyTilesVideoToggle;

  public MeetingPage(WebDriver webDriver, Logger logger) throws KiteTestException {
    super(webDriver, logger);
  }

  public String getPeerConnectionScript() {
    return "window.pc = [];"
        + "map = APP.conference._room.rtc.peerConnections;"
        + "for(var key of map.keys()){"
        + "  window.pc.push(map.get(key).peerconnection);"
        + "}";
  }

  public void clickVideoToggle() {
    manyTilesVideoToggle.click();
  }

  public void videoIsPublishing(int timeout) throws TimeoutException {
    WebDriverWait wait = new WebDriverWait(webDriver, timeout);
    wait.until(ExpectedConditions.visibilityOf(videos.get(0)));
  }

  public int numberOfVideos() {
    return videos.size();
  }

  public List<WebElement> getVideoElements() {
    return videos;
  }

  public void changeLocalDisplayName(String userId) {
    if (userId == null) {
      // default
      ((JavascriptExecutor) webDriver)
          .executeScript("APP.conference.changeLocalDisplayName(APP.conference.getMyUserId())");
    }
    ((JavascriptExecutor) webDriver)
        .executeScript("APP.conference.changeLocalDisplayName(\"" + userId + "\")");
  }
}

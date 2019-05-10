package io.cosmosoftware.kite.jitsi.pages;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.pages.BasePage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;

public class MeetingPage extends BasePage {

  private final int numberOfParticipants;

  @FindBy(id = "largeVideo")
  private WebElement mainVideo;

  @FindBy(tagName = "video")
  private List<WebElement> videos;

  @FindBy(xpath = "//*[@id=\"new-toolbox\"]/div[2]/div[3]/div[1]/div/div")
  private WebElement manyTilesVideoToggle;

  public MeetingPage(WebDriver webDriver, Logger logger) throws KiteTestException {
    super(webDriver, logger);
    this.numberOfParticipants =
      Integer.parseInt(executeJsScript(webDriver, getNumberOfParticipantScript()).toString()) + 1;
  }
  
  public String getNumberOfParticipantScript() {    
    return "return APP.conference.getNumberOfParticipantsWithTracks();";
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
  
  
  public int numberOfVideos() {
    return videos.size();
  }
  
  public int getNumberOfParticipants() {
    return this.numberOfParticipants;
  }

  public List<WebElement> getVideoElements(){ return videos;}
  
}

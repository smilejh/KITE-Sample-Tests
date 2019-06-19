package io.cosmosoftware.kite.openvidu.pages;

import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MeetingPage extends BasePage {
  
  public MeetingPage(Runner runner) {
    super(runner);
  }

  @FindBy(tagName = "video")
  private List<WebElement> videos;

  public List<WebElement> getVideoElements() {
    return this.videos;
  }

  public int numberOfVideos(){
    return videos.size();
  }
}

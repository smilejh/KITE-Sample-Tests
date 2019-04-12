package io.cosmosoftware.kite.simulcast.pages;

import io.cosmosoftware.kite.simulcast.LoopbackStats;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public abstract class SimulcastPageBase {


  protected final Logger logger = Logger.getLogger(this.getClass().getName());
  protected final WebDriver webDriver;

  @FindBy(tagName="video")
  private List<WebElement> videos;

  protected SimulcastPageBase(WebDriver webDriver) {
    this.webDriver = webDriver;
  }

  /**
   *
   * @return the list of video elements
   */
  public List<WebElement> getVideoElements() {
    return videos;
  }

  public abstract void clickButton(String rid, int tid);

  public abstract LoopbackStats getLoopbackStats();

}

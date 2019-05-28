package io.cosmosoftware.kite.simulcast.pages;

import io.cosmosoftware.kite.pages.BasePage;
import io.cosmosoftware.kite.simulcast.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public abstract class SimulcastPageBase extends BasePage {


  @FindBy(tagName="video")
  private List<WebElement> videos;

  protected SimulcastPageBase(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
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

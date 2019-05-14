package io.cosmosoftware.kite.janus.pages;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import io.cosmosoftware.kite.pages.BasePage;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class JanusPage extends BasePage {

  private final String PUBLISHING = "//b[text()='Publishing...']";


  //not used for now
  @FindBy(xpath = "//a[@class='dropdown-toggle']")
  private WebElement demosListDropdown;
  
  @FindBy(tagName="video")
  private List<WebElement> videos;

  @FindBy(xpath=PUBLISHING)
  private WebElement publishing;

  @FindBy(id="start")
  private WebElement startStopButton;

  @FindBy(xpath = "//h3[contains(text(),'Local Stream')]")
  private WebElement localStreamHeader;

  @FindBy(id="curbitrate")
  private WebElement currentBitRatePrint;

  @FindBy(id="curres")
  private WebElement currentResolutionPrint;

  public JanusPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
  }

  //not needed for now
  public void openDemosListDropdown() throws KiteInteractionException {
    waitUntilVisibilityOf(demosListDropdown, 2);
    click(demosListDropdown);

  }

  public void startDemo () throws KiteInteractionException {
    waitUntilVisibilityOf(startStopButton, 2);
    click(startStopButton);
  }

  /**
   *  ensure that the demo page is displayed
    * @param timeoutInSeconds
   * @throws KiteInteractionException if the element is not visible within the timeout
   */
  public void waitForLocalStreamHeaderVisibility (int timeoutInSeconds) throws KiteInteractionException {
    waitUntilVisibilityOf(localStreamHeader, timeoutInSeconds);

  }



  /**
   *
   * @param timeout
   * @throws TimeoutException if the element is not invisible within the timeout
   */
  public void videoIsPublishing(int timeout) throws TimeoutException {
    WebDriverWait wait = new WebDriverWait(webDriver, timeout);
    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(PUBLISHING)));
    wait.until(ExpectedConditions.invisibilityOf(element));
  }

  /**
   *
   * @return the list of video elements
   */
  public List<WebElement> getVideoElements() {
    return videos;
  }


  /**
   * Load the web page at url
   * @param url the url of the page to load
   */
  public void load(String url) {

    loadPage(webDriver, url, 20);

    //try reloading 3 times as it sometimes gets stuck at 'publishing...'
    for (int i = 0; i < 3; i++) {
      try {
        this.videoIsPublishing( 10);
        logger.info("Page loaded successfully");
        break;
      } catch (TimeoutException e) {
        logger.warn(" reloading the page (" + (i + 1) + "/3)");
        loadPage(webDriver, url, 20);
      }
    }
  }

}

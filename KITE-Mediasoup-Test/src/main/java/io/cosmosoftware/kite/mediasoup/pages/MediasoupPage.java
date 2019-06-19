package io.cosmosoftware.kite.mediasoup.pages;

import io.cosmosoftware.kite.pages.BasePage;
import io.cosmosoftware.kite.report.KiteLogger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class MediasoupPage extends BasePage {

  
  @FindBy(tagName="video")
  private List<WebElement> videos;


  public MediasoupPage(WebDriver webDriver, KiteLogger logger) {
    super(webDriver, logger);
    PageFactory.initElements(webDriver, this);
  }


  /**
   *
   * @param timeout
   * @throws TimeoutException if the element is not invisible within the timeout
   */
  public void videoIsPublishing(int timeout) throws TimeoutException {
    WebDriverWait wait = new WebDriverWait(webDriver, timeout);
    wait.until(ExpectedConditions.visibilityOf(videos.get(0)));
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

    //try reloading 3 times as it sometimesgets stuck at 'publishing...'
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

  public void setUserId(String userId){
    ((JavascriptExecutor) webDriver).executeScript("CC.changeDisplayName(\"" + userId +"\")");
  }



}

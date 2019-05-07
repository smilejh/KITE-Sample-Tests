package io.cosmosoftware.kite.janus.pages;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import io.cosmosoftware.kite.pages.BasePage;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;
import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class JanusPage extends BasePage {

  private final String PUBLISHING = "//b[text()='Publishing...']";
  
  @FindBy(tagName="video")
  private List<WebElement> videos;

  @FindBy(xpath=PUBLISHING)
  private WebElement publishing;

  @FindBy(id="unpublish")
  private WebElement unpublish;

  public JanusPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
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

  /**
   * Make GetStats script into executable string through executeJsScript
   *
   * @param getStatsFile
   * @param testName
   * @param testId
   */

  private String statsScriptToString (StringBuilder getStatsFile, String testName, String testId) {

    String[] split = getStatsFile.toString().split(", KITETestName");
    String split_1 = ", \"" + testName + "\"" + split[1];
    String split_2 = ", \"" + testName + "\"" + split[2];
    String[] split1 = split_1.split(", KITETestId");
    String split_1_1 = ", \"" + testId + "\"" + split1[1];
    String[] split2 = split_2.split(", KITETestId");
    String split_2_1 = ", \"" + testId + "\"" + split2[1];
    String getStatsScript = split[0] + split1[0] + split_1_1 + split2[0] + split_2_1;

    System.out.println("Converted to string");
    return getStatsScript;
  }

  /**
   * Load GetStats script into browser
   *
   * @param getStatsFile
   * @param testName
   * @param testId
   */
  public String loadGetStats (StringBuilder getStatsFile, String testName, String testId) {
    System.out.println("Executing Javascript Script");
    return (String) executeJsScript(webDriver, statsScriptToString(getStatsFile, testName, testId));
  }

  public void unpublish() throws KiteInteractionException {
    click(unpublish);
  }

}

package io.cosmosoftware.kite.janus.pages;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import io.cosmosoftware.kite.pages.BasePage;
import io.cosmosoftware.kite.janus.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.StringTokenizer;

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

  /**
   * Echo Test
   */

  //when simulcast=true

  @FindBy(id="sl-0")
  private WebElement sl0Button;

  @FindBy(id="sl-1")
  private WebElement sl1Button;

  @FindBy(id="sl-2")
  private WebElement sl2Button;

  @FindBy(id="tl-0")
  private WebElement tl0Button;

  @FindBy(id="tl-1")
  private WebElement tl1Button;

  @FindBy(id="tl-2")
  private WebElement tl2Button;

  /**
   * Streaming Test
   */

  @FindBy(id="streamset")
  private WebElement streamSetButton;

  @FindBy(id="watch")
  private WebElement streamWatchButton;

  @FindBy(id="1")
  private WebElement streamVideoSet;

  @FindBy(id="2")
  private WebElement streamAudioSet;

  @FindBy(id="3")
  private WebElement streamVideoOnDemandSet;


  /**
   * VideoCall Test and VideoRoom Test
   */

  @FindBy(id="username")
  private WebElement callerNameField;

  @FindBy(id="peer")
  private WebElement peerNameField;

  @FindBy(id="register")
  private WebElement userRegisterButton;

  @FindBy(id="call")
  private WebElement callHangupButton;

  @FindBy(xpath = "//button[contains(text(),'Answer')]")
  private WebElement answerButton;

  @FindBy(xpath = "//button[contains(text(),'OK')]")
  private WebElement waitingForPeerToAnswerButton;


  public JanusPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
  }

  //not needed for now
  public void openDemosListDropdown() throws KiteInteractionException {
    waitUntilVisibilityOf(streamSetButton, 2);
    click(streamSetButton);

  }

  public void openStreamSetList() throws KiteInteractionException {
    waitUntilVisibilityOf(streamSetButton, 2);
    click(streamSetButton);
  }

  public void selectStreamSet(String streamSet) throws KiteInteractionException {
    switch (streamSet) {
      case "videoLive":
        click(streamVideoSet);
        break;
      case "audioLive":
        click(streamAudioSet);
        break;
      case "videoOnDemand":
        click(streamVideoOnDemandSet);
        break;
    }
  }

  public void launchStreaming() throws KiteInteractionException {
    click(streamWatchButton);
  }

  public void joinVideoCall() throws KiteInteractionException {
    click(streamWatchButton);
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
   *
   * Click a button
   *
   * @param rid the rid
   * @param tid the tid
   */

  public void clickButton(String rid, int tid) throws KiteInteractionException {
    switch (rid) {
      case "a":
        click(sl2Button);
        break;
      case "b":
        click(sl1Button);
        break;
      case "c":
        click(sl0Button);
        break;
    }
    switch (tid) {
      case 0:
        click(tl0Button);
        break;
      case 1:
        click(tl1Button);
        break;
      case 2:
        click(tl2Button);
        break;
      default:
        break;
    }
  }

  //should be removed or adapted for the test on demo version
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

  public LoopbackStats getLoopbackStats() {
    String r = currentResolutionPrint.getText();
    StringTokenizer st = new StringTokenizer(r, "x");
    return new LoopbackStats("1280", "720", "0", "0", st.nextToken(), st.nextToken(), "0", "0");
  }

  public void fillCallerName(String userName) throws KiteInteractionException {
    waitUntilVisibilityOf(callerNameField, 2);
    sendKeys(callerNameField, userName);
  }

  public void fillPeerName(String userName) throws KiteInteractionException {
    waitUntilVisibilityOf(peerNameField, 2);
    sendKeys(peerNameField, userName);
  }
  
  public void callPeer() throws KiteInteractionException {
    click(callHangupButton);    
  }

  public void registerUser() throws KiteInteractionException {
    waitUntilVisibilityOf(userRegisterButton, 1);
    click(userRegisterButton);
  }

  public String getVideoIdByIndex(int i) {
    return videos.get(i).getAttribute("id");
  }

  public void answerCall () throws KiteInteractionException {
    waitUntilVisibilityOf(answerButton,10);
    click(answerButton);
  }

  public void acceptAlert() {
    try {
      WebDriverWait wait = new WebDriverWait(this.webDriver, 4);
      wait.until(ExpectedConditions.alertIsPresent());
      Alert alert = this.webDriver.switchTo().alert();
      logger.info("Alert: " + alert.getText());
      alert.accept();
    } catch (Exception e) {
        return;
    }
  }

  public String getAlertText() throws TimeoutException {
    WebDriverWait wait = new WebDriverWait(this.webDriver, 4);
    wait.until(ExpectedConditions.alertIsPresent());
    Alert alert = this.webDriver.switchTo().alert();
    return "Alert text: " +alert.getText();
  }

  public void waitForWaitingAnswerAlert(int timeoutInSeconds) throws KiteInteractionException {
    waitUntilVisibilityOf(waitingForPeerToAnswerButton,timeoutInSeconds);
  }


  public void waitUntilPeerAnswer(int timeoutInSeconds) throws TimeoutException {
    WebDriverWait wait = new WebDriverWait(webDriver, timeoutInSeconds);
    wait.until(ExpectedConditions.invisibilityOf(waitingForPeerToAnswerButton));
  }
  
}

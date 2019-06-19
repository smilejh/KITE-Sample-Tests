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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


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
  private WebElement acceptAlertButton;


  @FindBy(className = "bootbox-body")
  private WebElement alertText;


  @FindBy(id="myvideo")
  private WebElement localVideo;

  private boolean userRegistered = true;

  private List<Integer> remoteUserIndexList = new ArrayList<>();

  public JanusPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
  }

  public void setRegistrationState (boolean registered){
    userRegistered = registered;
  }

  public boolean getRegistrationState (){
    return userRegistered;
  }

  public void startOrStopDemo () throws KiteInteractionException {
    waitUntilVisibilityOf(startStopButton, 2);
    click(startStopButton, true);
  }

  /**
   * wait until the local video is published and has finished publishing
   * @param timeout
   * @throws TimeoutException if the element is not invisible within the timeout
   */
  public void firstVideoIsPublishing(int timeout) throws TimeoutException {
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

  public void waitUntilVisibilityOfFirstVideo(int timeoutInSeconds) throws KiteInteractionException {
    By locator = By.tagName("video");
    waitUntilVisibilityOf(locator, timeoutInSeconds);
  }

  public String getVideoIdByIndex(int i) {
    return videos.get(i).getAttribute("id");
  }

  /**
   * Streaming test
   */
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


  public void startOrStopDemo () throws KiteInteractionException {
    waitUntilVisibilityOf(startStopButton, 2);
    click(startStopButton, true);
  }

  /**
   * wait until the local video is published and has finished publishing
   * @param timeout
   * @throws TimeoutException if the element is not invisible within the timeout
   */
  public void firstVideoIsPublishing(int timeout) throws TimeoutException {
    WebDriverWait wait = new WebDriverWait(webDriver, timeout);
    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(PUBLISHING)));
    wait.until(ExpectedConditions.invisibilityOf(element));
  }


  public void waitUntilVisibilityOfFirstVideo(int timeoutInSeconds) throws KiteInteractionException {
    By locator = By.tagName("video");
    waitUntilVisibilityOf(locator, timeoutInSeconds);
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


  public void answerCall () throws KiteInteractionException {
    waitUntilVisibilityOf(answerButton,10);
    click(answerButton);
  }

  public String acceptAlert() throws KiteInteractionException {
    try {
      waitUntilVisibilityOf(acceptAlertButton,2);

    } catch (Exception e) {
      return "No alert";
    }
    String text = alertText.getText();
    click(acceptAlertButton);
    return "Text of the alert : " + text ;
  }

  public void waitForWaitingAnswerAlert(int timeoutInSeconds) throws KiteInteractionException {
    waitUntilVisibilityOf(acceptAlertButton,timeoutInSeconds);
  }


  public void waitUntilPeerAnswer(int timeoutInSeconds) throws TimeoutException {
    WebDriverWait wait = new WebDriverWait(webDriver, timeoutInSeconds);
    wait.until(ExpectedConditions.invisibilityOf(acceptAlertButton));
  }

  /**
   * videoroom test
   */



  /**
   * videoroom test
   *  get the name of the user whose video is displayed at the given index
   * @param index should be not greater than 5 (only 6 users can register in the video room)
   * @return
   */
  public String getRemoteUserNameByIndex (int index){
    By locator = By.id("remote" + index );
    return webDriver.findElement(locator).getText();
  }

  /**
   *  get the list of index corresponding to a user registered for the test
   */

  public void setUserIndexList (){
    String name;
    for (int i=1; i<6; i++ ){
      name = getRemoteUserNameByIndex(i);
      logger.info("remote user name = " + name);
      if (!(name == null)&&!(name.isEmpty())){
        if (name.contains("user")){
          remoteUserIndexList.add(i);
        }
      }
    }
  }

  public List<String> getRemotePC (){
    List<String> peerConnectionsList = new ArrayList<String>();
    for (int i: remoteUserIndexList){
      peerConnectionsList.add("feeds["+ i + "].webrtcStuff.pc");
    }
    return peerConnectionsList;
    }


  public List<String> getRemoteVideoIdList (){
    List<String> remoteIdList = new ArrayList<String>();
    for (int i: remoteUserIndexList){
          remoteIdList.add("remotevideo"+ i);
    }
    return remoteIdList;
  }

  public List<WebElement> getTestUsersVideos() {
    List<WebElement> registeredVideo = new ArrayList<>();
    registeredVideo.add(localVideo);
    for (String videoId: getRemoteVideoIdList()){
      By locator = By.id(videoId);
      registeredVideo.add(webDriver.findElement(locator));
    }
    return registeredVideo;
  }
}

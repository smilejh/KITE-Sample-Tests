package io.cosmosoftware.kite.simulcast.pages;

import io.cosmosoftware.kite.entities.Timeouts;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.simulcast.LoopbackStats;
import io.cosmosoftware.kite.util.TestUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.StringTokenizer;

import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;

public class JanusLoopbackPage extends SimulcastPageBase {

  private final WebDriverWait wait;

  private final String START_BUTTON = "start";

  @FindBy(id=START_BUTTON)
  private WebElement startButton;

  @FindBy(id="peervideo")
  private WebElement peerVideo;

  @FindBy(id="myvideo")
  private WebElement myVideo;

  @FindBy(id="curres")
  private WebElement resolution;


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


  @FindBy(id="high")
  private WebElement high;
  @FindBy(id="medium")
  private WebElement medium;
  @FindBy(id="low")
  private WebElement low;


  public JanusLoopbackPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
    this.wait = new WebDriverWait(webDriver, Timeouts.TEN_SECOND_INTERVAL);
    PageFactory.initElements(webDriver, this);
  }

  /**
   * Click the start button
   */
  public void clickButton() {
    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(START_BUTTON)));
    startButton.click();
    wait.until(ExpectedConditions.visibilityOf(peerVideo));
  }


  /**
   * Gets the LoopbackStats
   *
   * @return LoopbackStats
   */
  @Override
  public LoopbackStats getLoopbackStats() {
    String r = resolution.getText();
    StringTokenizer st = new StringTokenizer(r, "x");
    return new LoopbackStats("1280", "720", "0", "0",
          st.nextToken(), st.nextToken(), "0", "0");
  }



  /**
   *
   * Click a button
   *
   * @param rid the rid
   * @param tid the tid
   */
  @Override
  public void clickButton(String rid, int tid) {
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

  /**
   * Clicks the button and wait until it's class is set to btn-success.
   *
   * @param button
   */
  @Override
  public void click(WebElement button) {
    if (button.getAttribute("class").contains("btn-success")) {
      logger.info("skipping button " + button.getText() + " since class = " + button.getAttribute("class"));
      return;
    }
    button.sendKeys(Keys.ENTER);
    int ct = 0;
    while(button.getAttribute("class").contains("btn-success") && ct < 10) {
      TestUtils.waitAround(Timeouts.ONE_SECOND_INTERVAL);
      ct++;
    }
  }


  /**
   * Set the bitrate cap (in bps)
   * @param str the bitrate cap in bps
   */
  public void setBitrateCap(String str) throws KiteTestException {
    String command = "$('#cap').html('" + str + "');echotest.send({message: {bitrate:" + str + "}})";
    executeJsScript(webDriver, command);
    logger.info("bitrate cap set to " + str + "bps");
  }


  /**
   * @return true if the low bitrate is higher than the medium bitrate
   */
  public boolean lowHigherThanMedium() {
    int l =  Integer.parseInt(low.getText());
    int m =  Integer.parseInt(medium.getText());
    return l != 0 && m != 0 && ( l > m);
  }

  /**
   * @return true if the medium bitrate is higher than the high bitrate
   */
  public boolean mediumHigherThanHigh() {
    int h =  Integer.parseInt(high.getText());
    int m =  Integer.parseInt(medium.getText());
    return h != 0 && m != 0 && ( m > h);
  }

}

package io.cosmosoftware.kite.hangouts.pages;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.pages.BasePage;
import io.cosmosoftware.kite.report.Status;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.Set;

import static io.cosmosoftware.kite.entities.Timeouts.*;
import static io.cosmosoftware.kite.util.ReportUtils.getStackTrace;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;
import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class MainPage extends BasePage {

  @FindBy(xpath = "//*[@id=\"gb_70\"]")
  WebElement signinButton;

  @FindBy(xpath = "//*[@id=\"identifierId\"]")
  WebElement emailInput;

  @FindBy(xpath = "//*[@id=\"password\"]/div[1]/div/div[1]/input")
  WebElement passwordInput;

  @FindBy(xpath = "//*[@id=\"identifierNext\"]/span/span")
  WebElement idNextButton;

  @FindBy(xpath = "//*[@id=\"passwordNext\"]/span/span")
  WebElement pwNextButton;

  @FindBy(xpath = "//*[@id=\"yDmH0d\"]/div[8]/div[2]/div/div[3]")
  WebElement nextButton;

  @FindBy(xpath = "//*[@id=\"yDmH0d\"]/div[4]/div[4]/div/div/ul/li[1]/div[1]")
  WebElement videoCallButton;

  @FindBy(xpath = " //*[@id=\"yDmH0d\"]/div[6]/div/div[2]/span/div/div[3]/div[2]/div/span/span")
  WebElement copyToClipboard;

  public MainPage(Runner runner) {
    super(runner);
  }
  
  public void open(String url) {
    loadPage(webDriver, url, 20);
  }
  
  public void clickSignIn() throws KiteInteractionException {
    waitUntilVisibilityOf(signinButton, TEN_SECOND_INTERVAL_IN_SECONDS);
    click(signinButton);    
  }

  public void enterEmail(String email) throws KiteInteractionException {
    waitUntilVisibilityOf(emailInput, TEN_SECOND_INTERVAL_IN_SECONDS);
    sendKeys(emailInput, email);
    clickNext(idNextButton);
  }

  public void enterPassword(String password) throws KiteInteractionException {
    waitUntilVisibilityOf(passwordInput, TEN_SECOND_INTERVAL_IN_SECONDS);
    sendKeys(passwordInput, password);
    clickNext(pwNextButton);
    //if first time login, we need to skip the presentation
    skipPresentation();
  }

  private void clickNext(WebElement nextButton) throws KiteInteractionException {
    waitUntilVisibilityOf(nextButton, TEN_SECOND_INTERVAL_IN_SECONDS);
    click(nextButton);
  }
  
  private void skipPresentation() {
    try {
      clickNext(nextButton);
      clickNext(nextButton);
      clickNext(nextButton);
      clickNext(nextButton);
    } catch (KiteInteractionException e) {
      logger.debug("No presentation to skip.");
    }
  }


  public void startVideoCall() throws KiteInteractionException {
    waitUntilVisibilityOf(videoCallButton, TEN_SECOND_INTERVAL_IN_SECONDS);
    String parentWinHandle = webDriver.getWindowHandle();
    click(videoCallButton);
    waitAround(THREE_SECOND_INTERVAL);
    new WebDriverWait(webDriver,
      TEN_SECOND_INTERVAL_IN_SECONDS).until(
      d -> {
    Set<String> winHandles = d.getWindowHandles();
    for (String w:winHandles) {
      if (!w.equals(parentWinHandle)) {
        d.switchTo().window(w);
        waitAround(ONE_SECOND_INTERVAL);
        return true;
      }
    }
    return false;
    });
  }
  
  public String getClipboardText() throws KiteTestException {

    try {
      Clipboard c= Toolkit.getDefaultToolkit().getSystemClipboard();
      return (String) c.getData(DataFlavor.stringFlavor);
    } catch (Exception e) {
      logger.error(getStackTrace(e));
      throw new KiteTestException("Error retrieving clipboard text", Status.BROKEN, e);
    }
//    // Get current window handle
//    waitUntilVisibilityOf(copyToClipboard, TEN_SECOND_INTERVAL_IN_SECONDS);
//    click(copyToClipboard);
//    return (String) executeJsScript(webDriver, 
//      "window.text = \"empty\";" + 
//        "navigator.clipboard.readText().then(text => { return window.text = text;}); return window.text;");    
  }

}

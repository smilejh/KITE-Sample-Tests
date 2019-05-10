package io.cosmosoftware.kite.janus.pages;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DemoJanusPage extends JanusPage {

  @FindBy(id="start")
  private WebElement startStopButton;





  public DemoJanusPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
  }

  public void joinCall () throws KiteInteractionException {
    click(startStopButton);
  }



}

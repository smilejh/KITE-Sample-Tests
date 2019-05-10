package io.cosmosoftware.kite.jitsi.pages;

import io.cosmosoftware.kite.pages.BasePage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class JoinPage extends BasePage {
  public JoinPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
  }

  public void joinRoom(String url) {
    webDriver.manage().window().maximize();
    webDriver.get(url);
  }
}

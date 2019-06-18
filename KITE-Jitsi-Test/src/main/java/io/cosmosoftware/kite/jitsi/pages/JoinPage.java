package io.cosmosoftware.kite.jitsi.pages;

import io.cosmosoftware.kite.pages.BasePage;
import io.cosmosoftware.kite.report.KiteLogger;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class JoinPage extends BasePage {
  public JoinPage(WebDriver webDriver, KiteLogger logger) {
    super(webDriver, logger);
  }

  public void joinRoom(String url) {
    webDriver.manage().window().maximize();
    loadPage(webDriver, url, 10);
    logger.info("Open " + url);
  }
}

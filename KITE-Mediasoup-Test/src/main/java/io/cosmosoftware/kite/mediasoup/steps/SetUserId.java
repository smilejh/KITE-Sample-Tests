package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.mediasoup.pages.MediasoupPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class SetUserId extends TestStep {
  String userId;
  public SetUserId(WebDriver webDriver,String userId) {
    super(webDriver);
    this.userId = userId;
  }

  @Override
  public String stepDescription() {
    return "Setting Display name for User";
  }

  @Override
  protected void step() throws KiteTestException {
    MediasoupPage page = new MediasoupPage(webDriver, logger);
    page.setUserId(userId);
  }
}

package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class SetUserIdStep extends TestStep {
  String userId;
  public SetUserIdStep(WebDriver webDriver, String userId) {
    super(webDriver);
    this.userId = userId;
  }

  @Override
  public String stepDescription() {
    return "Setting Display Name to " + this.userId;
  }

  @Override
  protected void step() throws KiteTestException {
    MeetingPage page = new MeetingPage(webDriver, logger);
    page.changeLocalDisplayName(userId);
  }

}

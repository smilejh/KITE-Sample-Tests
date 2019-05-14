package io.cosmosoftware.kite.openvidu.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.openvidu.pages.JoinPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class JoinRoomStep extends TestStep {

  public JoinRoomStep(WebDriver webDriver) {
    super(webDriver);
  }

  @Override
  public String stepDescription() {
    return "Joining Room";
  }

  @Override
  protected void step() throws KiteTestException {
    JoinPage joinPage = new JoinPage(webDriver, logger);
    joinPage.clickJoinButton();
  }
}

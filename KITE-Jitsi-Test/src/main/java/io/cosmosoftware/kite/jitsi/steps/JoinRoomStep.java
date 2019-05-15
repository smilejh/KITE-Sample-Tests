package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.pages.JoinPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinRoomStep extends TestStep {
  protected String roomUrl;

  public JoinRoomStep(WebDriver webDriver, String roomUrl) {
    super(webDriver);
    this.roomUrl = roomUrl;
  }

  @Override
  public String stepDescription() {
    return "Joining a Jitsi Room";
  }

  @Override
  protected void step() throws KiteTestException {
    JoinPage page = new JoinPage(webDriver, logger);
    page.joinRoom(roomUrl);
  }
}

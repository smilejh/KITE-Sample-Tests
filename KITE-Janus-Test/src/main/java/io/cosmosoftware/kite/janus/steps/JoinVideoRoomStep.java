package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinVideoRoomStep extends TestStep {
  private final String userName;

  public JoinVideoRoomStep(WebDriver webDriver, String userName) {
    super(webDriver);
    this.userName = userName;
  }

  @Override
  protected void step() throws KiteTestException {
    final JanusPage janusPage = new JanusPage(webDriver, logger);
    janusPage.fillCallerName(userName);
    janusPage.registerUser();
//    janusPage.acceptAlert();

  }

  @Override
  public String stepDescription() {
    return "Enter a user name and join the video room";
  }
}

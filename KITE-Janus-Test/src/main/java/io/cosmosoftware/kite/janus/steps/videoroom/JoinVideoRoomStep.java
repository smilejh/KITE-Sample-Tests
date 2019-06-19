package io.cosmosoftware.kite.janus.steps.videoroom;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinVideoRoomStep extends TestStep {
  private final String userName;
  private final JanusPage janusPage;

  public JoinVideoRoomStep(WebDriver webDriver, String userName, JanusPage janusPage) {
    super(webDriver);
    this.userName = userName;
    this.janusPage = janusPage;
  }

  @Override
  protected void step() throws KiteTestException {
    janusPage.setRegistrationState(false);
    janusPage.fillCallerName(userName);
    janusPage.registerUser();
    String alertText = janusPage.acceptAlert();
    logger.info(alertText);
    if (alertText.equalsIgnoreCase("No alert")){
      janusPage.setRegistrationState(true);
    }
  }

  @Override
  public String stepDescription() {
    return "Enter a user name and join the video room";
  }
}

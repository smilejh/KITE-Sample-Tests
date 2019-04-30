package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import io.cosmosoftware.kite.jitsi.pages.JoinPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class JoinRoomStep extends TestStep {
  protected String roomId;

  public JoinRoomStep(WebDriver webDriver){
    super(webDriver);
  }

  public void setRoomId(String roomId){
    this.roomId = roomId;
  }

  @Override
  public String stepDescription() {
    return "Joining a Jitsi Room";
  }

  @Override
  protected void step() throws KiteTestException {
    JoinPage page = new JoinPage(webDriver, logger);
    page.joinRoom(KiteJitsiTest.url.endsWith("/")? (KiteJitsiTest.url) : (KiteJitsiTest.url + "/") + roomId);
  }
}

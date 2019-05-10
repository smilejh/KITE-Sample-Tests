package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import io.cosmosoftware.kite.jitsi.pages.JoinPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class JoinRoomStep extends TestStep {
  protected  String url;
  protected String roomId;

  public JoinRoomStep(WebDriver webDriver, String url, String roomId) {
    super(webDriver);
    this.url = url;
    this.roomId = roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  @Override
  public String stepDescription() {
    return "Joining a Jitsi Room";
  }

  @Override
  protected void step() throws KiteTestException {
    JoinPage page = new JoinPage(webDriver, logger);
    page.joinRoom(
        url.endsWith("/") ? url : (url + "/") + roomId);
  }
}

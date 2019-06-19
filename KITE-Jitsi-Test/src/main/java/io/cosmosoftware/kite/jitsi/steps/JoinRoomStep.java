package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.jitsi.pages.JoinPage;
import io.cosmosoftware.kite.steps.TestStep;

public class JoinRoomStep extends TestStep {
  
  protected String roomUrl;
  private final JoinPage page;

  public JoinRoomStep(Runner runner, String roomUrl) {
    super(runner);
    this.roomUrl = roomUrl;
    this.page = new JoinPage(runner);
  }

  @Override
  public String stepDescription() {
    return "Joining a Jitsi Room";
  }

  @Override
  protected void step() throws KiteTestException {
    page.joinRoom(roomUrl);
  }
}

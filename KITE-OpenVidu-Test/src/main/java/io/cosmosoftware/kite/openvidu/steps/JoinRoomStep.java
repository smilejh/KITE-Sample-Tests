package io.cosmosoftware.kite.openvidu.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.openvidu.pages.JoinPage;
import io.cosmosoftware.kite.steps.TestStep;

public class JoinRoomStep extends TestStep {

  private final JoinPage joinPage;
  
  public JoinRoomStep(Runner runner) {
    super(runner);
    this.joinPage = new JoinPage(runner);
  }

  @Override
  public String stepDescription() {
    return "Joining Room";
  }

  @Override
  protected void step() throws KiteTestException {
    joinPage.clickJoinButton();
  }
}

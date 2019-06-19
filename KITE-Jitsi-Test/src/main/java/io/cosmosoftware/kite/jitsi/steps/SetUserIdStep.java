package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.steps.TestStep;

public class SetUserIdStep extends TestStep {
  
  private final String userId;
  private final MeetingPage meetingPage;
  
  public SetUserIdStep(Runner runner, String userId) {
    super(runner);
    this.userId = userId;
    this.meetingPage = new MeetingPage(runner);
  }

  @Override
  public String stepDescription() {
    return "Setting Display Name to " + this.userId;
  }

  @Override
  protected void step() throws KiteTestException {
    meetingPage.changeLocalDisplayName(userId);
  }

}

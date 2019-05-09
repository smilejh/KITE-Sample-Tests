package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.util.TestUtils;
import org.openqa.selenium.WebDriver;

public class StayInMeetingStep extends TestStep {

  private final int meetingDuration;

  public StayInMeetingStep(WebDriver webDriver, int meetingDuration) {
    super(webDriver);
    this.meetingDuration = meetingDuration;
  }
  
  
  @Override
  public String stepDescription() {
    return "Stay in the meeting for " + meetingDuration + "s.";
  }
  
  @Override
  protected void step() throws KiteTestException {
    TestUtils.waitAround(meetingDuration * 1000);
  }
}

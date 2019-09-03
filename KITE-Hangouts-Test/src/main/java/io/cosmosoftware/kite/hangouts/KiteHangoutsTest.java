package io.cosmosoftware.kite.hangouts;

import io.cosmosoftware.kite.hangouts.checks.AllVideoCheck;
import io.cosmosoftware.kite.hangouts.checks.FirstVideoCheck;
import io.cosmosoftware.kite.hangouts.pages.MainPage;
import io.cosmosoftware.kite.hangouts.steps.JoinVideoCallStep;
import io.cosmosoftware.kite.hangouts.steps.OpenUrlStep;
import io.cosmosoftware.kite.hangouts.steps.StartVideoCallStep;
import io.cosmosoftware.kite.steps.*;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import javax.json.JsonArray;

import static io.cosmosoftware.kite.util.WebDriverUtils.isChrome;

public class KiteHangoutsTest extends KiteBaseTest {

  private JsonArray users;
  
  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    if (this.payload != null) {
      users = this.payload.getJsonArray("users");
    }
  }
  

  @Override
  public void populateTestSteps(TestRunner runner) {
    //sign in to hangout
    MainPage mainPage = new MainPage(runner);
    runner.addStep(new OpenUrlStep(runner, mainPage, url, 
      users.getJsonObject(runner.getId()).getString("user"),
      users.getJsonObject(runner.getId()).getString("pass")));
    //first user starts the video call, others wait
    runner.addStep(new StartVideoCallStep(runner, this.roomManager, mainPage));
    if (this.windowWidth > 0 && this.windowHeight > 0) {
      runner.addStep(new ResizeWindowStep(runner, this.windowWidth, this.windowHeight));
    }
    //all users wait for the call to be started.
    runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));
    runner.addStep(new JoinVideoCallStep(runner, this.roomManager, mainPage));
    runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));

    runner.addStep(new FirstVideoCheck(runner, mainPage));
    runner.addStep(new AllVideoCheck(runner, getMaxUsersPerRoom(), mainPage));
    runner.addStep(new ScreenshotStep(runner));
    if (this.meetingDuration > 0) {
      runner.addStep(new StayInMeetingStep(runner, meetingDuration));
    }
    if (isChrome(runner.getWebDriver())) {
      runner.addStep(new WebRTCInternalsStep(runner));
    }
  }

  
  
}

package io.cosmosoftware.kite.jitsi;

import io.cosmosoftware.kite.jitsi.steps.JoinRoomStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import java.util.Random;

public class KiteJitsiTest extends KiteBaseTest {
  public static String url = "https://meet.jit.si";
  final Random rand = new Random(System.currentTimeMillis());
  final String roomId = String.valueOf(Math.abs(rand.nextLong()));

  @Override
  protected void populateTestSteps(TestRunner runner) {
    JoinRoomStep joinRoomStep = new JoinRoomStep(runner.getWebDriver());
    joinRoomStep.setRoomId(roomId);
    runner.addStep(joinRoomStep);
    
  }
}

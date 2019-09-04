package io.cosmosoftware.kite.hangouts.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.hangouts.pages.MainPage;
import io.cosmosoftware.kite.manager.RoomManager;
import io.cosmosoftware.kite.steps.TestStep;
import org.webrtc.kite.tests.TestRunner;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.entities.Timeouts.THREE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class StartVideoCallStep extends TestStep {
  private final int id;
  private final MainPage mainPage;
  private final RoomManager roomManager;
  
  public StartVideoCallStep(TestRunner runner, RoomManager roomManager, MainPage mainPage) {
    super(runner);
    this.id = runner.getId();
    this.mainPage = mainPage;
    this.roomManager = roomManager;
  }





  @Override
  public String stepDescription() {
    if (id == 0) {
      return "Starting the video call";
    } else {
      return "Waiting for the video call to start";
    }
  }

  @Override
  protected void step() throws KiteTestException {
    if (id % roomManager.getUsersPerRoom() == 0) {
      mainPage.startVideoCall();
      waitAround(ONE_SECOND_INTERVAL);
      String roomUrl = webDriver.getCurrentUrl();
      roomManager.setDynamicUrl(id, roomUrl);
      logger.info("Video call started on url: " + roomUrl);
    } else {
      waitAround(THREE_SECOND_INTERVAL);
    }    
    
  }
  
}

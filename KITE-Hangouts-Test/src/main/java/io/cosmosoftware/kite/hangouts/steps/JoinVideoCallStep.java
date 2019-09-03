package io.cosmosoftware.kite.hangouts.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.hangouts.pages.MainPage;
import io.cosmosoftware.kite.manager.RoomManager;
import io.cosmosoftware.kite.steps.TestStep;
import org.webrtc.kite.tests.TestRunner;

import static io.cosmosoftware.kite.entities.Timeouts.THREE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class JoinVideoCallStep extends TestStep {

  private final int id;
  private final RoomManager roomManager;
  private final MainPage mainPage;
  
  
  
  public JoinVideoCallStep(TestRunner runner, RoomManager roomManager, MainPage mainPage) {
    super(runner);
    this.id = runner.getId();
    logger.info( "TestRunner id = " + this.id + " userPerRoom = " + roomManager.getUsersPerRoom());
    this.roomManager = roomManager;
    this.mainPage = mainPage;
  }
  
  
  @Override
  public String stepDescription() {
    return "Joining the call";
  }
  
  @Override
  protected void step() throws KiteTestException {
    if (id % roomManager.getUsersPerRoom() == 0) {
      waitAround(THREE_SECOND_INTERVAL);
    } else {
      String roomUrl = roomManager.getDynamicUrl(id);
      logger.info("Joining call at: " + roomUrl);
      mainPage.open(roomUrl);
      mainPage.clickJoin();
    }
  }
}

import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import junit.framework.TestCase;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.config.Browser;
import org.webrtc.kite.config.EndPoint;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.entities.Timeouts.TEN_SECOND_INTERVAL;
import static org.webrtc.kite.Utils.getEndPointList;

public class KiteJitsiTestTest extends TestCase {
  private static final String jitsiUrl = "https://meet.jit.si";
  private static final String SELENIUM_SERVER_URL = "http://localhost:4444/wd/hub";
  private static final String CONFIG_FILE = "configs/jitsi.json";
  List<EndPoint> endPoints = new ArrayList<>(getEndPointList(CONFIG_FILE, "browsers"));

  private JsonObject getFakePayload () {
    return Json.createObjectBuilder()
        .add("url", jitsiUrl)
        .add("statsCollectionDuration", TEN_SECOND_INTERVAL * 2)
        .add("statsCollectionInterval", ONE_SECOND_INTERVAL * 2)
        .add("printToCSV", false)
        .add("printToJson", false)
//        .add("selectedStats", getFakeSelectedStat())
        .build();
  }

  public void testTestScript() throws Exception {
    setUp();
    KiteJitsiTest test = new KiteJitsiTest();
    test.setEndPointList(endPoints);
    test.setPayload(getFakePayload());
    test.execute();
  }
}

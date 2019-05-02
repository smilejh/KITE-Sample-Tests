import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import junit.framework.TestCase;
import org.webrtc.kite.config.EndPoint;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;

import static org.webrtc.kite.Utils.getEndPointList;

public class KiteJitsiTestTest extends TestCase {
  private static final String CONFIG_FILE = "configs/jitsi.json";
  List<EndPoint> endPoints = new ArrayList<>(getEndPointList(CONFIG_FILE, "browsers"));

  public JsonObject setFakePayload(){
    JsonObjectBuilder builder = Json.createObjectBuilder();
    builder.add("getStats", true);
    builder.add("statsCollectionTime", 10);
    builder.add("statsCollectionInterval", 1);
    return builder.build();
  }

  public void testTestScript() throws Exception {
    setUp();
    KiteJitsiTest test = new KiteJitsiTest();
    test.setEndPointList(endPoints);
    test.setPayload(setFakePayload());
    test.execute();
  }
}

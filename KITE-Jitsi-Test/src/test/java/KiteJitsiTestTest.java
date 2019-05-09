import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import junit.framework.TestCase;
import org.webrtc.kite.config.EndPoint;
import org.webrtc.kite.tests.KiteBaseTest;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;

import static org.webrtc.kite.Utils.getEndPointList;
import static org.webrtc.kite.Utils.getPayload;

public class KiteJitsiTestTest extends TestCase {
  private static final String CONFIG_FILE = "configs/jitsi.json";
  List<EndPoint> endPointList = new ArrayList<>(getEndPointList(CONFIG_FILE, "browsers"));

  public JsonObject setFakePayload() {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    builder.add("getStats", true);
    builder.add("statsCollectionTime", 5);
    builder.add("statsCollectionInterval", 1);
    arrayBuilder.add("inbound-rtp");
    arrayBuilder.add("outbound-rtp");
    arrayBuilder.add("candidate-pair");
    builder.add("selectedStats", arrayBuilder.build());
    return builder.build();
  }

  public void testTestScript() throws Exception {
    KiteBaseTest test = new KiteJitsiTest();
    test.setPayload(getPayload(CONFIG_FILE, 0));
    test.setEndPointList(endPointList);
    JsonObject testResult = test.execute();
  }
}

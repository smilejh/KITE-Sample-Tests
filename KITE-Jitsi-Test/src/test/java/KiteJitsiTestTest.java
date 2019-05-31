import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import junit.framework.TestCase;
import org.webrtc.kite.config.Tuple;
import org.webrtc.kite.tests.KiteBaseTest;

import javax.json.JsonObject;

import static org.webrtc.kite.Utils.getEndPointList;
import static org.webrtc.kite.Utils.getPayload;

public class KiteJitsiTestTest extends TestCase {
  private static final String CONFIG_FILE = "configs/local.jitsi.config.json";
  private final Tuple endPointList = getEndPointList(CONFIG_FILE, "browsers");

  public void testTestScript() throws Exception {
    KiteBaseTest test = new KiteJitsiTest();
    test.setPayload(getPayload(CONFIG_FILE, 0));
    test.setEndPointList(endPointList);
    JsonObject testResult = test.execute();
  }
}

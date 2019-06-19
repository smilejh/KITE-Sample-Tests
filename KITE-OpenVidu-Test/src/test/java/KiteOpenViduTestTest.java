import io.cosmosoftware.kite.openvidu.KiteOpenViduTest;
import junit.framework.TestCase;
import org.webrtc.kite.config.test.Tuple;
import org.webrtc.kite.tests.KiteBaseTest;

import static org.webrtc.kite.Utils.getFirstTuple;
import static org.webrtc.kite.Utils.getPayload;

public class KiteOpenViduTestTest extends TestCase {
  private static final String CONFIG_FILE = "configs/local.openvidu.config.json";
  private Tuple tuple = getFirstTuple(CONFIG_FILE);
  
  
  public void testTestScript() throws Exception {
    KiteBaseTest test = new KiteOpenViduTest();
    test.setPayload(getPayload(CONFIG_FILE, 0));
    test.setTuple(tuple);
    Object testResult = test.execute();
  }
}

import io.cosmosoftware.kite.openvidu.KiteOpenViduTest;
import junit.framework.TestCase;
import org.webrtc.kite.config.client.Client;
import org.webrtc.kite.config.Tuple;
import org.webrtc.kite.tests.KiteBaseTest;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

import static org.webrtc.kite.Utils.*;

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

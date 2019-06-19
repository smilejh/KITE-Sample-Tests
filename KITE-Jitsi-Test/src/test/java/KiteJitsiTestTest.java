import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import junit.framework.TestCase;
import org.webrtc.kite.config.test.Tuple;
import org.webrtc.kite.tests.KiteBaseTest;

import static org.webrtc.kite.Utils.getFirstTuple;
import static org.webrtc.kite.Utils.getPayload;

public class KiteJitsiTestTest extends TestCase {
  private static final String CONFIG_FILE = "configs/local.jitsi.config.json";
  private Tuple tuple = getFirstTuple(CONFIG_FILE);
  
  
  public void testTestScript() throws Exception {
    KiteBaseTest test = new KiteJitsiTest();
    test.setPayload(getPayload(CONFIG_FILE, 0));
    test.setTuple(tuple);
    Object testResult = test.execute();
  }
}

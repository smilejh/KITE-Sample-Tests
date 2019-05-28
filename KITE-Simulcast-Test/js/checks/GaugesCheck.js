const {TestUtils, TestStep} = require('kite-common');
/**
 * Class: GaugesCheck
 * Extends: TestStep
 * Description:
 */
class GaugesCheck extends TestStep {
  constructor(kiteBaseTest, rid, tid) {
    super();
    this.driver = kiteBaseTest.driver;
    this.rid = rid;
    this.tid = tid;
    this.page = kiteBaseTest.page;
    this.takeScreenshot = kiteBaseTest.takeScreenshot;
    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return 'Gauges values for profile ' + this.rid + this.tid;
  }

  async step() {

    let loopBackStats = await this.page.getLoopbackStats();

    // Data
    this.testReporter.textAttachment(this.report, 'stats', JSON.stringify(loopBackStats), "json");

    if (this.takeScreenshot) {
      let screenshot = await TestUtils.takeScreenshot(this.driver);
      this.testReporter.screenshotAttachment(this.report, "Screenshot step", screenshot); 
    }
  }
}

module.exports = GaugesCheck;
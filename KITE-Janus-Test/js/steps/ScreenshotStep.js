const {TestUtils, TestStep} = require('kite-common');

/**
 * Class: ScreenshotStep
 * Extends: TestStep
 * Description:
 */
class ScreenshotStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;

    // Test reporter if you want to add attachment(s)
    this.testReporter = kiteBaseTest.reporter;
  }

  stepDescription() {
    return 'Get a screenshot';
  }

  async step() {
    let screenshot = await TestUtils.takeScreenshot(this.driver);
    this.testReporter.screenshotAttachment(this.report, "Screenshot step", screenshot); 
  }
}

module.exports = ScreenshotStep;
const {TestStep} = require('kite-common');

// Starting a session for classroom demo
class StartSessionStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.page = kiteBaseTest.page;
    this.id = kiteBaseTest.id;
  }

  stepDescription() {
    return "Start a session";
  }

  async step() {
    await this.page.startLesson(this.id);
  }
}

module.exports = StartSessionStep;
const {TestStep} = require('kite-common');

class OpenJitsiUrlStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.url = kiteBaseTest.url;
    this.uuid = kiteBaseTest.uuid;
    this.page = kiteBaseTest.page;
  }

  stepDescription() {
    return 'Open https://meet.jit.si/ and enter in the room';
  }

  async step() {
    await this.page.open(this);
    await this.page.enterRoom("ChooseYourOwnRoomName" + this.uuid);
  }
}

module.exports = OpenJitsiUrlStep;
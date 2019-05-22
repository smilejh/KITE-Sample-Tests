const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common'); 
const {JoinVideoCallStep, GetStatsStep, ScreenshotStep} = require('./steps');
const {FirstVideoCheck, AllVideoCheck} = require('./checks');
const {MediasoupPage} = require('./pages');

// KiteBaseTest config
const globalVariables = TestUtils.getGlobalVariables(process);
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);


function getRoomUrl(id) {
  const roomid = Math.floor(id / payload.usersPerRoom);
  if (roomid > payload.rooms.length) {
    console.error('Not enough rooms');
    return;
  }
  return payload.url + payload.rooms[roomid] + '&username=user' + Array(Math.max(3 - String(id).length + 1, 0)).join(0) + id;
}



class Mediasoup extends KiteBaseTest {
  constructor(name, globalVariables, capabilities, payload) {
    super(name, globalVariables, capabilities, payload);
    this.page = new MediasoupPage();
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);

      let joinVideoCallStep = new JoinVideoCallStep(this, getRoomUrl(this.id));
      await joinVideoCallStep.execute(this);
      
      let firstVideoCheck = new FirstVideoCheck(this);
      await firstVideoCheck.execute(this);

      let allVideoCheck = new AllVideoCheck(this);
      await allVideoCheck.execute(this);

      let getStatsStep = new GetStatsStep(this);
      await getStatsStep.execute(this);

      let screenshotStep = new ScreenshotStep(this);
      await screenshotStep.execute(this);

      await TestUtils.waitAround(5000 * this.numberOfParticipant); // 5s per participant

    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports = Mediasoup;

let test = new Mediasoup('Mediasoup test', globalVariables, capabilities, payload);
test.run();

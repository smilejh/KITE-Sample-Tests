const {TestStep, TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;

class StartVideoCallStep extends TestStep {

  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.url = kiteBaseTest.url;
    this.uuid = kiteBaseTest.uuid;
    this.page = kiteBaseTest.page;
    this.id = kiteBaseTest.id;
    this.io = kiteBaseTest.io;
    this.usersPerRoom = kiteBaseTest.payload.usersPerRoom;
  }

  stepDescription() {
    if (this.id % this.usersPerRoom == 0) {
      return "Starting the video call";
    } else {
      return "Waiting for the video call to start";
    }
  }

  async step() {  
    if (this.id % this.usersPerRoom == 0) {
      await this.page.startVideoCall();
      await waitAround(1000);
      const roomUrl = await this.driver.getCurrentUrl();
      this.io.emit("registerRoomUrl", roomUrl, this.id);
      console.log("Video call started on url: " + roomUrl);
    } else {
      await waitAround(3 * 1000);
    } 
    await waitAround(2000); 
  }
}

module.exports = StartVideoCallStep;
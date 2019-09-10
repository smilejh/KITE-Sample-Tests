const {TestStep, TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;



const getRoomUrl = async function(io) {
  let waiting = true;
  let i = 0;
  let roomUrl = null;
  while(waiting && i < 10) {
    io.on("url", function(url) {
      waiting = false;
      console.log('server responded url = ' + url);
      roomUrl = url;
    });
    i++;
    await TestUtils.waitAround(1000); // waiting 1 sec
  }
  return roomUrl;
}

class JoinVideoCallStep extends TestStep {

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
    this.roomUrl = null;
  }

  stepDescription() {
    if (this.id % this.usersPerRoom != 0) {
      return "Joining the video call";
    } else {
      return "Waiting for others to join the video call";
    }
  }

  async step() {
  
    if (this.id % this.usersPerRoom != 0) {
      const d = this.driver;
      await this.io.emit("getRoomUrl", this.id, this.usersPerRoom);
      this.roomUrl = await getRoomUrl(this.io);
      console.log("this.roomUrl  = " + this.roomUrl );
      await this.driver.get(this.roomUrl);
      await waitAround(1000); 
      this.page.clickJoin();
    } else {
      await waitAround(3 * 1000);
    } 
    await waitAround(2000); 
  }
  
}

module.exports = JoinVideoCallStep;
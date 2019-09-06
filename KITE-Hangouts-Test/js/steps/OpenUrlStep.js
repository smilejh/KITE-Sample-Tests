const {TestStep, TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;

class OpenUrlStep extends TestStep {

  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.timeout = kiteBaseTest.timeout;
    this.url = kiteBaseTest.url;
    this.uuid = kiteBaseTest.uuid;
    this.page = kiteBaseTest.page;
    this.id = kiteBaseTest.id;
    this.user = kiteBaseTest.payload.users[this.id].user;
    this.pass = kiteBaseTest.payload.users[this.id].pass;
  }

  stepDescription() {
    return 'Open ' + this.url + '';
  }

  async step() {
    await this.page.open(this);
    await this.page.open(this);
    await this.page.clickSignIn();
    await this.page.enterEmail(this.user);
    await this.page.enterPassword(this.pass);    
    await waitAround(2000); 
  }
}

module.exports = OpenUrlStep;
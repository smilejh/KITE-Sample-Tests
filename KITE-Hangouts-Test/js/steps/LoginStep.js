const {TestStep, TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;

class LoginStep extends TestStep {

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
    this.driver.manage().window().setRect(540, 960, 960 * (this.id % 2), 540 * (( this.id - (this.id % 2))/2));
    await this.page.clickSignIn();
    await this.page.enterEmail(this.user);
    await this.page.enterPassword(this.pass);    
    await waitAround(2000); 
  }
}

module.exports = LoginStep;
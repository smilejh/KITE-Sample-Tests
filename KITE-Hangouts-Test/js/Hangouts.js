const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('./node_modules/kite-common'); 
const {OpenUrlStep} = require('./steps');
const {MyFirstCheck} = require('./checks');
const {MainPage} = require('./pages');

class Hangout extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new MainPage(this.driver);

      let openUrlStep = new OpenUrlStep(this);
      await openUrlStep.execute(this);

//      let myFirstCheck = new MyFirstCheck(this);
//      await myFirstCheck.execute(this);


      let screenshotStep = new ScreenshotStep(this);
      await screenshotStep.execute(this);
      
      
    } catch (e) {
      console.log('Exception in testScript():' + e);
    } finally {
      await this.driver.quit();
    }
  }
}

module.exports= Hangout;

(async () => {
  const kiteConfig = await TestUtils.getKiteConfig(__dirname);
  let test = new Hangout('Hangout test', kiteConfig);
  console.log('Test starting');
  await test.run();
  console.log('Test completed');
})();

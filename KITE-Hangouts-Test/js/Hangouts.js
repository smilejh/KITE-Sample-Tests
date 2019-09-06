const {TestUtils, WebDriverFactory, KiteBaseTest, ScreenshotStep} = require('./node_modules/kite-common'); 
const {LoginStep, StartVideoCallStep, JoinVideoCallStep} = require('./steps');
const {FirstVideoCheck, AllVideoCheck} = require('./checks');
const {MainPage} = require('./pages');

class Hangout extends KiteBaseTest {
  constructor(name, kiteConfig) {
    super(name, kiteConfig);
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.remoteUrl);
      this.page = new MainPage(this.driver);

      let loginStep = new LoginStep(this);
      await loginStep.execute(this);
      
      let startVideoCallStep = new StartVideoCallStep(this);
      await startVideoCallStep.execute(this);      
      await this.waitAllSteps();
      
      let joinVideoCallStep = new JoinVideoCallStep(this);
      await joinVideoCallStep.execute(this);      
      await this.waitAllSteps();
      
      let firstVideoCheck = new FirstVideoCheck(this);
      await firstVideoCheck.execute(this);

      let allVideoCheck = new AllVideoCheck(this);
      await allVideoCheck.execute(this);

      await this.waitAllSteps();
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

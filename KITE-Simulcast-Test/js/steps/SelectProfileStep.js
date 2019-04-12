const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');

const map = promise.map;

module.exports = {
	execute: async function(driver, testReport, rid, tid) {
		let report = {};
		let result = 'passed';
		let attachment = {};
		attachment['type'] = 'json';
    report['name'] = 'Choose a profile id gauge :' + rid + ';' + tid;
    report['start'] = Date.now();
    if (testReport.status === 'passed') {
			try {
        console.log('executing: ' + report.name);
				const button = await driver.findElement(By.xpath('//button[@data-rid="' + rid + '" and @data-tid="' + tid + '"]'));
				button.sendKeys(Key.ENTER);
      } catch (error) { // need better handling
        result = 'failed';
        testReport['status'] = result;
        attachment['value'] = '' + error;
        console.log(error);
      }
    } else {
      result = 'skipped';
      console.log('skipping: ' + report.name);
    }
    report['attachment'] = attachment;
    report['status'] = result;
    report['stop'] = Date.now();
    testReport.steps.push(report);
	}
}
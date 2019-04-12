const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');

const map = promise.map;

module.exports = {
	execute: async function(driver, testReport, url, timeout) {
		let report = {};
		let result = 'passed';
		let attachment = {};
		attachment['type'] = 'json';
    report['name'] = 'Open ' + url + ' wait for page to load';
    report['start'] = Date.now();
    if (testReport.status === 'passed') {
			try {
        console.log('executing: ' + report.name);
        await driver.get(url);
        console.log('wait for page to be loaded');
        await driver.wait(async function() {
          const s = await driver.executeScript("return document.readyState");
          return s === "complete";
        }, timeout);
        console.log('Page loaded');
      } catch (error) {
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
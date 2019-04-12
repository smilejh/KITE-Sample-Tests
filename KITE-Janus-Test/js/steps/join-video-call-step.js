const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');
const janusPage = require('../pages/janus-page')
const elements = janusPage.elements;
const map = promise.map;

module.exports = {
	execute: async function(driver, testReport, url, timeout) {

    // Report
		let report = {};
		let result = 'passed';
		let attachment = {};
		attachment['type'] = 'json';
    report['name'] = 'Open ' + url + ' and check the video';

    // Start of step
    report['start'] = Date.now();
    if (testReport.status === 'passed') {
			try {
        console.log('executing: ' + report.name);

        // Navigate to the url
        await driver.get(url);

        console.log('wait for page to be loaded');
        await TestUtils.waitForPage(driver, timeout);

        // Wait for publishing button
        console.log('Page loaded. Now wait for text "Publishing..."');
        await driver.wait(until.elementLocated(elements.publishingLocator));
        let details = {};

        // Verify videos
        const videoElements = await janusPage.getVideoElements(driver, timeout);

        const ids = await map(videoElements, e =>  e.getAttribute("id"));
		    ids.forEach(async function(id) {
		      const videoCheck = await TestUtils.verifyVideoDisplayById(driver, id);
		      details['videoCheck_' + id] = videoCheck;
		    });
        attachment['value'] = details;

      } catch (error) {
        result = 'failed';
        testReport['status'] = result;
        console.log(error);
      }
    } else {
      result = 'skipped';
      console.log('skipping: ' + report.name);
    }
    // End of the report
    report['stop'] = Date.now();
    report['attachment'] = attachment;
    report['status'] = result;
    testReport.steps.push(report);
	}
}
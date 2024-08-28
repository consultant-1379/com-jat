package jatbackend.service.jenkins

import grails.transaction.Transactional
import groovy.sql.Sql
import jatbackend.Interfaces.jenkins.ITestReport
import jatbackend.domain.Build
import jatbackend.domain.Test
import jatbackend.domain.TestClass

import java.sql.SQLException

@Transactional
/**
 * Used for processing the test report data, implements the ITestReport interface
 */
class TestReportService implements ITestReport {

	def dataSource
	def loggerService

	/**
	 * Processes the test report data
	 * @param testReport The JSON input data
	 * @param build The build object passed from the JenkinsController
	 * @return boolean returns true if test report created
	 */
	boolean processTestReportData(def testReport, Build build) {

		if(!testReport || !build) {
			return false
		}

		build.passCount = testReport.passCount
		build.skipCount = testReport.skipCount
		build.failCount = testReport.failCount
		build.save(flush:true)

		TestClass testClass = null
		Test test = null
		def sql = new Sql(dataSource)
		def testClassTmp
		String testClassStatus

		for(def cases in testReport.suites.cases) {

			for(def caseObj in cases) {

				TestClass newTestClass
				testClassStatus = "PASSED"

				if(!caseObj.className){
					loggerService.logError("Test class name is null")
					return false
				}

				if(build.testClasses){
					testClassTmp = TestClass.createCriteria().list{
						'in'("id", build.testClasses*.id)
						eq("className", caseObj.className)
					}
				}


				if(!caseObj.status.equals("PASSED") && !caseObj.status.equals("FIXED")){
					testClassStatus = "FAILED"
				}


				if(!testClassTmp){
					newTestClass = new TestClass(className: caseObj.className, status: testClassStatus).save()//String sqlInsertQuery = "INSERT INTO test_class values(0,'" + caseObj.className + "'" + ");"

					if(!build.addToTestClasses(newTestClass)) {
						loggerService.logError("Error binding test class to build")
						sql.close()
						return false
					}


					if(build.testClasses){
						testClassTmp = TestClass.createCriteria().list{
							'in'("id", build.testClasses*.id)
							eq("className", caseObj.className)
						}
					}

				} else {
					newTestClass = testClassTmp[0]

					if(newTestClass.status.equals("PASSED") && testClassStatus.equals("FAILED")){
						newTestClass.status = testClassStatus
						newTestClass.save(flush:true)
					}
				}
				
				
				test = new Test(
						testName: caseObj.name,
						errorStackTrace: caseObj.errorStackTrace,
						duration: caseObj.duration,
						failedSince: caseObj.failedSince,
						status: caseObj.status
						)

				if(!test.save()) {
					loggerService.logError("Error creating test")
					sql.close()
					return false
				}

				if(!newTestClass.addToTests(test)) {
					loggerService.logError("Error binding test to test class")
					sql.close()
					return false
				}


			}
		}

		sql.close()
		return true
	}
}


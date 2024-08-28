package jatbackend.service.retrievedata

import grails.transaction.Transactional
import jatbackend.Interfaces.retrievedata.ICompare;
import jatbackend.domain.Build
import jatbackend.domain.Job
import jatbackend.domain.Test
import jatbackend.domain.TestClass

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Used to retrieve tests with the same name from different builds
 * for comparison and analysis
 */
@Transactional
class CompareTestService implements ICompare {

	/**
	 * Used to retrieve testresults for comparison of a test in different builds
	 * @param testid    the id of the test to compare results of
	 * @return  returns a jsonobject, containing the buildnumbers and
	 * comparable testresults within those builds
	 */
	List compareTests(String testId, String jobId) {
		def comparableTests = []
		def buildObj = new JSONObject()

		Job job = Job.get(jobId)

		if(!job || job.jobType == "MATRIX" || !job.builds){
			return null
		}

		job.builds.each { Build build ->
			if(build.testClasses){
				build.testClasses.each{ TestClass testclass ->
					if(testclass.tests){
						def tests = Test.createCriteria().list{
							'in'("id", testclass.tests*.id)
							eq("testName", testId)
						}
						if(tests){
							buildObj.put("buildNumber", build.buildNumber)
							buildObj.put("test", tests)
							comparableTests.add(buildObj)
							buildObj = new JSONObject()
						}
					}
				}
			}
		}
		comparableTests.sort{
			it.buildNumber
		}
		if(!comparableTests) {   //Checks if no comparable tests were found
			return null
		} else {
			return comparableTests
		}
	}
}
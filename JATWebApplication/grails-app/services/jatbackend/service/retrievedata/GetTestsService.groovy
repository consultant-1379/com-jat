package jatbackend.service.retrievedata

import grails.transaction.Transactional
import jatbackend.Interfaces.retrievedata.IGetTests;
import jatbackend.domain.Build
import jatbackend.domain.Test
import jatbackend.domain.TestClass

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * This service is used for retrieving passed and failed tests within a testclass
 */
@Transactional
class GetTestsService implements IGetTests {

    /**
     * Used for retrieving tests with a certain status
     * @param testclass the testclass to retrieve the tests from
     * @param build the build that contains the tests and testclass
     * @param status    the tests with the status that will be listed
     * @return  the list with the test that fulfill the criteria
     */
    private List testsWithStatus(TestClass testclass, List status) {
        def testList = Test.createCriteria().list {
            'in'("id", testclass.tests*.id)
            'in'("status", status)
            order("testName", "asc")
        }
        if(!testList) {
            return null
        } else {
            return testList
        }
    }

    /**
     * Used for retrieving passed and failed tests within a specified build and testclass
     * @param testid    the identifier for the testclass to retrieve tests from
     * @param buildid   the identifier for the build to retrieve tests from
     * @return  returns a jsonobject with the passed and failed tests
     * or null if no tests were found
     */
    JSONObject getTests(String testClassId, String buildId) {
        Long bId = Long.parseLong(buildId)
        Long tId = Long.parseLong(testClassId)
        Build build = Build.get(bId)    //Get Build with buildid
        TestClass testClass = TestClass.get(tId)    //Get TestClass with testclassid
        def tests = new JSONObject()

        if(!build || !testClass || !testClass.tests) {
            //Checks if build or testClass wasn't found and if testClass doesn't contain any tests
            return null
        }

        if(build.testClasses.contains(testClass) && build && testClass) {
            /*Checks if build contains testClass and that build
             and testClass are not empty*/
            def passedTests = testsWithStatus(testClass, ["PASSED","FIXED"])
            //Gets tests within testClass in build and where the result is "Passed"
            
            def failedTests = Test.createCriteria().list {
                'in'("id", testClass.tests*.id)
                ne("status", "PASSED")
                ne("status", "FIXED")
                order("testName", "asc")
            }

            if(passedTests) {    //Checks if passedTests contains any passed tests
                tests.put("PASSED", passedTests)
            }

            if(failedTests) {
                tests.put("FAILED", failedTests)
            }
        }

        if(!tests) {    //Checks if no tests were found
            return null
        } else {
            return tests
        }
    }
}
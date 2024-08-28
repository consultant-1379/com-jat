package jatbackend.service.retrievedata

import grails.transaction.Transactional
import jatbackend.Interfaces.retrievedata.IGetTestClasses;
import jatbackend.domain.Build
import jatbackend.domain.TestClass

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * This service is used to retrieve passed and failed testclasses in a build
 */
@Transactional
class GetTestClassesService implements IGetTestClasses {

    /**
     * Used for retrieving passed and failed testclasses
     * @param buildid   the identifer for the build to retrieve the testclasses from
     * @return  jsonobject with the passed and failed testclasses in the build with buildid
     * or null if no testclasses were found
     */
    JSONObject getTestClassesWithStatus(String buildId) {
        Long bId = Long.parseLong(buildId)
        Build build = Build.get(buildId)	//Gets the Build with the specified buildid
        def testclasses = new JSONObject()
        if(!build || !build.testClasses) {    //Checks if no build were found
            return null
        }

        def passedTestClassesList = TestClass.createCriteria().list{
            'in'("id", build.testClasses*.id)
            eq("status", "PASSED")
            order("className", "asc")
        }

        def failedTestClassesList = TestClass.createCriteria().list{
            'in'("id", build.testClasses*.id)
            eq("status", "FAILED")
            order("className", "asc")
        }

        if(passedTestClassesList) { //Checks if passedTestClassesList is containing anything
            testclasses.put("PASSED", passedTestClassesList)
        }

        if(failedTestClassesList) {
            testclasses.put("FAILED", failedTestClassesList)
        }

        if(!testclasses) {
            //Checks if no testclasses have been added to the jsonobject
            return null
        } else {
            return testclasses
        }
    }
}
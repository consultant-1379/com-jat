package jatbackend.controller

import groovy.json.JsonSlurper
import jatbackend.domain.Build
import jatbackend.domain.Job

/** JenkinsController contains the logic for handling the JSON data
 * that the Jenkins Post Build plugin send.
 *
 * Current implementation of run:
 * 1) Create job
 * 2) Create build
 * 3) Bind build to job
 * 4) Create test report
 * 5) Render success
 *
 * If any of the following steps fails,
 * i.e a build or job was not returned, then render failure
 */
class JenkinsController {
    /** Define which HTTP method that is allowed on the run method
     * Other HTTP methods will return "HTTP 405 METHOD NOT ALLOWED" */
    static allowedMethods = [run:['POST', 'GET']]

    // The HTTP status codes is used in the render methods, to inform the result
    final int HTTP_CREATED_CODE = 201
    final int HTTP_FAILED_CODE = 400

    //The different data handling services
    def constructBuildService
    def processJobDataService
    def testReportService

    /** Log4j is used for logging different error, e.g. if a job is not created */
    def loggerService

    /** This function uses Log4j to log the specified error message
     * @param errorMessage The error message to be logged by Log4j
     */

    void returnStatus(int status, String message) {
        loggerService.logError(message)
        render(status: status, text: message)
    }

    /** Main entry point for JAT application */
    def run() {
        def input = request.JSON
        def testReport = request.JSON.TestExecutionData

        Job job = processJobDataService.processJobData(input)
        if(!job) {
            returnStatus(HTTP_FAILED_CODE, "Failed to create job in JAT")
            return
        }

        Build build = constructBuildService.processBuildData(input, job)
        if(!build) {
            returnStatus(HTTP_FAILED_CODE, "Failed to create build in JAT")
            return
        }

        boolean bindBuildToJob = job.addToBuilds(build)
        if(!bindBuildToJob) {
            returnStatus(HTTP_FAILED_CODE, "Failed to bind build to job in JAT")
            return
        }

        if(testReport){
            boolean createdTestReport = testReportService.processTestReportData(testReport, build)
            if(!createdTestReport) {
                returnStatus(HTTP_FAILED_CODE, "Failed to create test report")
                return
            }
        }

        render(status: HTTP_CREATED_CODE, text: "JAT execution successful")

    }
}



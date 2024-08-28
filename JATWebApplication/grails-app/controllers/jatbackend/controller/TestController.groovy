package jatbackend.controller

import grails.converters.JSON

/**
 * Manages web requests regarding retrieval of Test information
 */
class TestController {
    static responseFormats = ['json', 'xml']
    static allowedMethods = [getTests: 'GET', compare: 'GET']
    def GetTestsService
    def CompareTestService

    /**
     * Used for retrieving all passed and failed tests within a TestClass
     */
    def getTests() {
        def tests = GetTestsService.getTests(params.testclassid, params.buildid)
        if(!tests) {
            render(status: 204, text: "Tests not found")
        } else {
            render tests as JSON
            /*All passed and failed tests in the testsclass with testclassid
             and within the build with buildid*/
        }
    }

    /**
     * Used to compare a test performed in different builds in a job
     */
    def compare() {
        def comparison = CompareTestService.compareTests(params.testid, params.jobId)
        if(!comparison) {
            render(status: 204, text: "Tests to compare not found")
        } else {
            render comparison as JSON
            /*All the tests from different builds with the specified
             properties to be compared*/
        }
    }
}
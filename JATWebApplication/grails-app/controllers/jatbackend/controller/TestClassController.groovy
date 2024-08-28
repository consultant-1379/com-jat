package jatbackend.controller

import grails.converters.JSON

/**
 * Manages web requests regarding retrieval of TestClass information
 */
class TestClassController {
    static responseFormats = ['json', 'xml']
    static allowedMethods = [getTestClasses: 'GET']
    def GetTestClassesService

    /**
     * Used for retrieving all passed and failed TestClasses within a Build
     */
    def getTestClasses() {
        def testclasses = GetTestClassesService.getTestClassesWithStatus(params.buildid)
        if(!testclasses) {
            render(status: 204, text: "Testclasses not found")
        } else {
            render testclasses as JSON
            //All passed and failed TestClasses within the build with buildid
        }
    }
}
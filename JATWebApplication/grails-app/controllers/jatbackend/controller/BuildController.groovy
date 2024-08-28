package jatbackend.controller

import grails.converters.JSON

/**
 * Manages web requests regarding retrieval of Build information
 */
class BuildController {
    static responseFormats = ['json', 'xml']
    static allowedMethods = [index: 'GET',
        getJobWithBuilds: 'GET',
        getTotalCounts: 'GET',
        showBuild: 'GET']
    def GetJobsService
    def GetBuildService
    def GetTotalCountsService

    /**
     * Used for retrieving builds within a job
     */
    def index() {
        def builds = GetJobsService.buildsInJob(params.jobId)
        //Calls the buildsInJob method in GetJobsService service
        if(!builds) {
            render(status: 204, text: "No content")
        } else {
            render builds as JSON
            //contains the builds within the job with jobId
        }
    }

    /**
     * Used for retrieving recent build information in a job
     */
    def getJobWithBuilds() {
        def jobsObj = GetBuildService.recentBuildsInJobs()
		
        if(!jobsObj) {
            render(status: 204, text: "No content")
        } else {
            render jobsObj as JSON
            /*Contains all matrix and singleton jobs with their most recent
             successful, unsuccessful and recent build*/
        }
    }

    def getRecentBuildInChildJobs() {
        def jobsObj = GetBuildService.recentBuildInChildJobs(params.jobId)
        if(!jobsObj) {
            render(status: 204, text: "No content")
        } else {
            render jobsObj as JSON
        }
    }

    /**
     * Used for retrieving total pass, fail and skip count
     * for Matrix and Singleton jobs
     */
    def getTotalCounts() {
        def counts = GetTotalCountsService.getTotalCounts()
        if(!counts) {
            render(status: 204, text: "No content")
        } else {
            render counts as JSON
            //Contains all jobs and their total pass, fail and skip count
        }
    }

    /**
     * Used for retrieving a single build by its id
     */
    def showBuild() {
        def build = GetBuildService.showBuild(params.buildid)
        if(!build) {
            render(status: 204, text: "No content")
        } else {
            render build as JSON    //Build with buildid
        }
    }
}
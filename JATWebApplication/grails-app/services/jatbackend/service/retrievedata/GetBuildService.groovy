package jatbackend.service.retrievedata

import grails.transaction.Transactional
import jatbackend.Interfaces.retrievedata.IGetBuilds;
import jatbackend.domain.Build
import jatbackend.domain.Job

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Used to retrieve builds with certain criterias in Matrix and Singleton jobs
 */
@Transactional
class GetBuildService implements IGetBuilds {

    /**
     * Used for listing builds in a job where result equals the parameter result
     * @param job   the job from which the recent build will be retrieved
     * @param result    the result/status for the most recent build in the job
     * @return  returns the most recent build with the result "result" in job
     * or null if no build was found
     */
    private List recentBuildWithStatus(Job job, String result) {
        def buildWithStatus = Build.createCriteria().list {
            //Lists the most recent build with the given result in job
            'in'("id", job.builds*.id)
            eq("result", result)
            maxResults(1)
            order("id", "desc")
        }
        if(!buildWithStatus){
            //Checks if a build with the given result wasn't found
            return null
        } else {
            return buildWithStatus
        }
    }

    List recentBuildInChildJobs(String jobId){
        def jobObj = new JSONObject()
        def jobsObj = []
        def recentBuild
        Job job = Job.get(jobId)
        if(!job || !job.jobType=="MATRIX" || !job.childJobs){
            return null
        }

        job.childJobs.each { Job job2 ->
            recentBuild = null
            if(job2.builds){
                jobObj = new JSONObject()
                recentBuild = Build.createCriteria().list {
                    //Lists all builds in the given job
                    'in'("id", job2.builds*.id)
                    maxResults(1)
                    order("id", "desc")
                }
            }
            jobObj.put("jobName", job2.jobName)
            jobObj.put("jobType", job2.jobType)
            jobObj.put("build", recentBuild)
            jobsObj.add(jobObj)
        }

        jobsObj.sort {
            it.jobName
        }

        if(!jobsObj) {
            return null
        } else {
            return jobsObj
        }
    }

    /**
     * Used for retrieving the most recent build and recent builds with a
     * certain status(Passed or Failed) for every job that is not child job
     * @return  the most recent builds for a certain status and in general for all jobs
     * or null if jobs weren't found
     */
    List recentBuildsInJobs() {
        def jobsObj = []
        def jobObj
        def testData
        def builds
        def passChildJobName
        def failChildJobName
        def recentChildJobName
        def build1
        def build2
        def build3
        Build buildSuccess, buildUnsuccess, buildRecent
        int skipCount, passCount, failCount, duration
        def buildStatus

        def jobList = Job.createCriteria().list {
            //Lists all jobs that are not of the type Child
            ne("jobType", "CHILD")
            order("jobName", "asc")
        }
        if(!jobList) {  //Checks if jobs were not found
            return null
        }

        jobList.each { Job job ->   //Loops through the jobs in jobList
            buildSuccess = null
            buildUnsuccess = null
            buildRecent = null
            build1 = null
            build2 = null
            build3 = null
            builds = new JSONObject()
            jobObj = new JSONObject()
            testData = new JSONObject()
            passChildJobName = null
            failChildJobName = null
            recentChildJobName = null
            buildStatus = "SUCCESS"
			duration = 0
            skipCount = 0
            passCount = 0
            failCount = 0
            

            if(job.jobType.equals("MATRIX")) {
                /*Checks if job is a matrix job in order to check if looping
                 through child jobs is necessary*/
                if(job.childJobs){
                    job.childJobs.each { Job job2 ->
                        //Loops through child jobs within job
                        if(job2.builds) {
//                            build1 = recentBuildWithStatus(job2, "SUCCESS")
//                            //Gets the most recent build with the result "SUCCESS"
//                            if(!build1.equals(null) && (buildSuccess.equals(null) || buildSuccess.id < build1[0].id)) {
//                                /*Checks if a build with the result "SUCCESS was found and if
//                                 its timestamp is greater than the one of the previous recent build*/
//                                buildSuccess = build1[0]
//                                passChildJobName = job2.jobName
//                            }

                            build2 = recentBuildWithStatus(job2, "FAILURE")
                            if(!build2.equals(null) && (buildUnsuccess.equals(null) || buildUnsuccess.id < build2[0].id)) {
                                buildUnsuccess = build2[0]
                                failChildJobName = job2.jobName
                            }

                            build3 = Build.createCriteria().list{
                                'in'("id", job2.builds*.id)
                                maxResults(1)
                                order("id", "desc")
                            }

                            if(build3) {
                                skipCount += build3.skipCount
                                passCount += build3.passCount
                                failCount += build3.failCount
								duration += build3.duration 
                                if(!build3[0].result.equals("SUCCESS")) {
                                    buildStatus = build3[0].result

                                }
                            }

                            if(!build3.equals(null) && (buildRecent.equals(null) || buildRecent.id < build3[0].id)) {
                                buildRecent = build3[0]
                                recentChildJobName = job2.jobName
                            }
                        }
                    }
                }

                testData.put("skipCount", skipCount)
                testData.put("passCount", passCount)
                testData.put("failCount", failCount)
                testData.put("buildNumber", buildRecent.buildNumber)
                testData.put("result", buildStatus)
                testData.put("timestamp", buildRecent.timestamp)
				testData.put("duration", duration)
//                builds.put("SUCCESS", buildSuccess)
                builds.put("FAILURE", buildUnsuccess)
                builds.put("RECENT", testData)
                builds.put("failureChildJobName", failChildJobName)
                builds.put("successChildJobName", passChildJobName)
                builds.put("recentChildJobName", recentChildJobName)
                jobObj.put("builds", builds)
                jobObj.put("jobName", job.jobName)
                jobObj.put("jobType", job.jobType)

                jobsObj.add(jobObj)
            } else if(job.jobType.equals("SINGLETON")) {
                //Checks if job is a singleton job
                build1 = null
                build2 = null
                build3 = null
                if(job.builds){
                    build1 = recentBuildWithStatus(job, "SUCCESS")
                    if(!build1.equals(null)){   //Checks if a build was found
                        buildSuccess = build1[0]
                    }

                    build2 = recentBuildWithStatus(job, "FAILURE")
                    if(!build2.equals(null)){
                        buildUnsuccess = build2[0]
                    }

                    build3 = Build.createCriteria().list{
                        'in'("id", job.builds*.id)
                        maxResults(1)
                        order("id", "desc")
                    }

                    if(!build3.equals(null)){
                        buildRecent = build3[0]
                    }
                }

                builds.put("SUCCESS", buildSuccess)
                builds.put("FAILURE", buildUnsuccess)
                builds.put("RECENT", buildRecent)
                jobObj.put("builds", builds)
                jobObj.put("jobName", job.jobName)
                jobObj.put("jobType", job.jobType)
                jobsObj.add(jobObj)
            }
        }

        jobsObj.sort {
            it.jobName
        }

        if(!jobsObj){
            return null
        } else {
            return jobsObj
        }
    }

    /**
     * Used for retrieving a build by its id
     * @return  the build with given buildId or null if no build was found
     */
    Build showBuild(String buildId) {
        Build build = Build.get(buildId)    //Gets build with the given id
        if(!build){
            return null
        } else {
            return build
        }
    }
}
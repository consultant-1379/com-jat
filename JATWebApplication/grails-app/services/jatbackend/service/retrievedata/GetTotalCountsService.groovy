package jatbackend.service.retrievedata

import grails.transaction.Transactional
import jatbackend.Interfaces.retrievedata.IGetTotalCounts;
import jatbackend.domain.Build
import jatbackend.domain.Job

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * This service is used to retrieve the total pass-, fail- and skipcount
 * for Matrix and Singleton jobs
 */
@Transactional
class GetTotalCountsService implements IGetTotalCounts {

    /**
     * Used for getting the sum of a property among the builds in job
     * @param job   the job with the builds
     * @param count the property that will be summed
     * @return  the sum of the specified property or null if no sum was calculated
     */
    private List getCount(Job job, String count) {
        def totalCount = Build.createCriteria().list {
            //Lists the sum of count
            'in'("id", job.builds*.id)  //Among the builds in the given job
            projections {
                sum(count)  //Sum the given Build property
            }
        }
        if(!totalCount) {   //Checks if no sum was found/calculated
            return null
        } else {
            return totalCount
        }
    }

    /**
     * Used for calculating the total fail-, pass- and skipcount
     * for all matrix and singleton jobs
     * @return  a jsonobject, containing jobnames and their totalcounts
     * or null if no jobs were found
     */
    List getTotalCounts() {
        def countObj
        def countsObj  = []
        def sums
        int failSum, skipSum, passSum

        def jobList = Job.createCriteria().list {
            //Lists all jobs that are not the type Child
            ne("jobType", "child")
            order("jobName", "desc")
        }

        if(!jobList){   //Checks if no jobs were found
            return null
        }

        jobList.each { Job job ->   //Loops through the jobs in jobList
            sums = new JSONObject()
            countObj = new JSONObject()
            failSum = 0
            skipSum = 0
            passSum = 0

            if(job.jobType == "MATRIX" && job.childJobs) {
                /*Checks if job type is Matrix to check if
                 looping through child jobs is necessary*/
                job.childJobs.each { Job childjob ->
                    if(childjob.builds){
                    //Looping through child jobs in job
                    def failCount = getCount(childjob, "failCount")
                    //Gets sum of failCount among the builds in the child job
                    if(failCount)   //Checks if a failCount was returned
                        failSum += failCount
                    //Adds failCount to the failSum of the Matrix job

                    def skipCount = getCount(childjob, "skipCount")
                    if(skipCount)
                        skipSum += skipCount

                    def passCount = getCount(childjob, "passCount")
                    if(passCount)
                        passSum += passCount
                    }
                }
                sums.put("failCount", failSum)
                sums.put("skipCount", skipSum)
                sums.put("passCount", passSum)
                countObj.put("sums", sums)
                countObj.put("jobName", job.jobName)
                countsObj.add(countObj)
            } else if(job.jobType == "SINGLETON") {
                if(job.builds){
                    //Checks if job type is singleton
                    def failCount = getCount(job, "failCount")
                    //Gets sum of failCount among the builds in the singleton job
                    if(failCount)   //Checks if failCount was returned
                        failSum += failCount
                    //Adds failCount to the failSum of the Singleton job

                    def skipCount = getCount(job, "skipCount")
                    if(skipCount)
                        skipSum += skipCount

                    def passCount = getCount(job, "passCount")
                    if(passCount)
                        passSum += passCount
                }
                sums.put("failCount", failSum)
                sums.put("skipCount", skipSum)
                sums.put("passCount", passSum)
                countObj.put("sums", sums)
                countObj.put("jobName", job.jobName)
                countsObj.add(countObj)
            }
        }

        if(!countsObj) {
            return null
        } else {
            return countsObj
        }
    }
}
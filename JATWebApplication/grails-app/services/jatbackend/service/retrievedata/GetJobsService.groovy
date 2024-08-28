package jatbackend.service.retrievedata

import grails.transaction.Transactional
import jatbackend.Interfaces.retrievedata.IGetJobs;
import jatbackend.domain.Build
import jatbackend.domain.Job

/**
 * This service is used for both retrieving jobs and builds in a job
 */
@Transactional
class GetJobsService implements IGetJobs {

    /**
     * Used to retrieve all jobs except childjobs
     * @return  the list containing jobs that fulfill the criteria
     * or null if no jobs were found
     */
    List getJobs() {
        def jobs = Job.createCriteria() //Creates criteria
        def jobList = jobs.list {    //List all jobs that fulfill the criteria
            ne("jobType", "child")  //That are not of the job type "Child"
            order("jobName", "asc")
        }
        if(!jobList) {  //Checks if no jobs were found
            return null
        } else {
            return jobList
        }
    }

    /**
     * Used to retrieve a job that is containing the given jobId
     * @param   jobId the identifier for the requested job
     * @return  the list with the requested child jobs and all their attributes
     * or null if no parent of child jobs were found
     */
    List getChildJobs(String jobId) {
        def jobs = Job.createCriteria() //Creates criteria
        Job job = Job.findByJobName(jobId)  //Gets the Job with the given jobId
        if(!job || job.jobType!="MATRIX" || !job.childJobs){
            //Checks if a job wasn't found or if job type is not Matrix
            return null
        }
        def childJobList = jobs.list {   //List all jobs that fulfill the criteria
            'in'("jobName", job.childJobs*.jobName) //In child jobs within job
            order("jobName", "asc")
        }
        if(!childJobList) { //Checks if no child jobs were found
            return null
        } else {
            return childJobList
        }
    }

    /**
     * Used for fetching the builds within a given job
     * @param job   the job from which the builds are retrieved
     * @param orderBy   the property to order the list with builds by
     * @param ord   the order in the list of builds
     * @return  the list with the builds in the given job
     * or null if no builds were found
     */
    private List builds(Job job, String orderBy, String ord) {
        def buildList = Build.createCriteria().list {
            //Lists all the builds within the given job
            'in'("id", job.builds*.id)
            order(orderBy, ord)
        }
        if(!buildList) {
            return null
        } else {
            return buildList
        }
    }

    /**
     * Used for retrieving the builds in the job with jobId
     * @param jobId the identifier for the job that will be fetched
     * @return  the list with builds in the job with jobId
     * or null if job is of wrong job type or if no builds were found
     */
    List buildsInJob(String jobId) {
        Job job = Job.get(jobId)    //Gets Job with jobId
        def buildList
        if(job && (job.jobType=="SINGLETON" || job.jobType=="CHILD") && job.builds) {
            //Checks that a job was found and that it's a singleton or child job
            def result = builds(job, "id", "asc")
            buildList = result
        } else {
            return null
        }

        if(!buildList) {    //Checks if no builds were found
            return null
        } else {
            return buildList
        }
    }
}
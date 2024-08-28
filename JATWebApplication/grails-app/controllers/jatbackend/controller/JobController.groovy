package jatbackend.controller

import grails.converters.JSON
import jatbackend.domain.Job
import jatbackend.domain.JobView

/**
 * Manages web requests regarding retrieval of Job information
 */
class JobController {
	static responseFormats = ['json', 'xml']
	static allowedMethods = [index: 'GET',delete: 'DELETE']
	def GetJobsService

	/**
	 * Used for retrieving child jobs within a job if jobId is defined.
	 * Otherwise list all jobs that are not of the job type child.
	 */
	def index() {
		def jobs
		if(params.jobId) {   //Checks if jobId is specified
			jobs = GetJobsService.getChildJobs(params.jobId)
			//Gets child jobs of the job with jobId
		} else {
			jobs = GetJobsService.getJobs()
			//Gets all jobs that are not a child job
		}
		if(!jobs) {
			render(status: 204, text: "Jobs not found")
		} else {
			render jobs as JSON
		}
	}

	def delete() {

		Job job = Job.get(params.id)

		if(!job) {
			render(status: 204, text: 'No job to delete')
		}

		def jobViews = JobView.list()
		def jobViewJobs
		for(JobView jobView in jobViews) {

			jobViewJobs = jobView.jobs

			if(jobViewJobs) {
				for(Job jobInJobView in jobViewJobs) {
					if(jobInJobView.jobName.equals(job.jobName)) {
						jobView.removeFromJobs(jobInJobView)
						break
					}
				}
			}
		}


		job.delete(flush:true)

		if(!Job.get(params.id)) {
			render(status: 204, text: 'Deleted job')
		}

	}

}

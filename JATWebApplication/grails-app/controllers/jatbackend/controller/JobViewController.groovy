package jatbackend.controller

import grails.converters.JSON
import jatbackend.domain.Job
import jatbackend.domain.JobView

class JobViewController {

	static responseFormats = ['json', 'xml']
	static allowedMethods = [index: 'GET',
		save: 'POST',
		update: 'PUT',
		delete: 'DELETE']

	def index() {
		render JobView.list() as JSON
	}


	def show() {

		JobView jobView = JobView.get(params.id)

		if(!jobView) {
			render(status: 204, text: 'No job view found')
		}

		render jobView as JSON
	}

	def save() {
		
		if(JobView.findByViewName(request.JSON.viewName)) {
			render(status: 400, text: 'View already exist')
			return
		}
		
		JobView jobView = new JobView(request.JSON)

		
		if(!jobView.save(flush:true)) {
			render(status: 400, text: 'Data input error')
			return
		}

		render(status: 201, text: jobView as JSON)

	}



	def update(JobView) {

	}

	def delete() {

		JobView jobView = JobView.get(params.id)

		if(!jobView) {
			render(status: 204, text: 'No job view found')
		}

		jobView.delete(flush:true)

		if(!JobView.get(params.id)){
			render(status: 204, text: 'Successfully removed job view')
		}

		render(status: 400, text: 'Failed to remove job view')

	}

	def addjob() {

		JobView jobView = JobView.get(request.JSON.jobViewId)
		Job job = Job.get(request.JSON.jobName)

		if(!job || !jobView) {
			render(status: 204, text: 'No job or view found')
		}

		def viewJobs = jobView.jobs

		if(viewJobs) {
			if(viewJobs.contains(job)) {
				jobView.removeFromJobs(job)
				render(status: 201, text: 'Successfully removed binding')
				return
			}
		}

		jobView.addToJobs(job)

		render(status: 201, text: 'Successfully binded job to view')

	}

	def jobviews() {

		JobView jobView = JobView.get(params.id)

		if(!jobView) {
			render(status: 204, text: 'No job or view found')
			return
		}

		def jobViews = jobView.jobs

		if(!jobViews) {
			render(status: 204, text: 'No job views found')
			return
		}

		render jobViews as JSON

	}
}

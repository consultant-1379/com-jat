package jatbackend.service.jenkins

import grails.transaction.Transactional
import jatbackend.Interfaces.jenkins.IBuild
import jatbackend.domain.Build
import jatbackend.domain.Job
import jatbackend.domain.TestClass

import org.codehaus.groovy.grails.web.json.JSONArray

@Transactional
/**
 * Constructs the build, this class implements the IBuild Interface
 */
class ConstructBuildService implements IBuild {

	def loggerService
	/**
	 * Used for constructing a build
	 * @param input The JSON input from the JenkinsController
	 * @return Build returns a build if created
	 */
	Build processBuildData(def input, Job job) {


		/**
		 * This arraylist contains all the required attributes for the build object
		 */
		ArrayList requiredAttributes = new ArrayList()

		//Add all the required fields from the input object, together with a description
		requiredAttributes.add(["Build data", input])
		requiredAttributes.add(["Build number", input.number])
		requiredAttributes.add(["Build timestamp", input.timestamp])
		requiredAttributes.add(["Build result", input.result])
		requiredAttributes.add(["Build duration", input.duration])

		for(def inputObj in requiredAttributes) {
			if(!inputObj[1]) {
				loggerService.logError("Required build attribute missing: " + inputObj[0])
				return null
			}
		}

		if(!job) {
			loggerService.logError("Required job missing in build")
			return null
		}

		String gitSHA1 = ""
		String gitBranchName = ""
		String user = ""
		String builtOnMachineName = ""
		String result = input.result
		String buildUrl = input.url
		int buildNumber = input.number
		int duration =  input.duration
		long timeStamp = input.timestamp

		if(input.builtOn) {
			builtOnMachineName = input.builtOn
		}

		JSONArray actions = input.actions
		def gitInformation = null

		for(int i = 0; i < actions.size(); i++){
			if(!actions[i].getClass().toString().toLowerCase().contains("null")){
				//Can not be checked in other way

				if(actions[i].lastBuiltRevision){
					if(actions[i].lastBuiltRevision.branch){
						gitInformation = actions[i].lastBuiltRevision.branch
					}
				}
			}
		}

		String itStr = "" //Iteration value of string

		if(gitInformation) {
			if(gitInformation[0] != null) {
				gitInformation[0].each{
					itStr = it.toString().toLowerCase() //Lower case so that no mismatch occurs
					if(itStr.startsWith("sha1=")) {
						gitSHA1 = itStr.substring(itStr.indexOf("=")+1)
					}
					if(itStr.startsWith("name=")) {
						gitBranchName = itStr.substring(itStr.indexOf("=")+1)
					}
				}
			}
		}


		def jobBuilds = job.builds
		Build currentBuild = Build.findByBuildNumber(buildNumber)
		if(jobBuilds) {
			if(jobBuilds.contains(currentBuild)) {

				currentBuild.user = user
				currentBuild.duration = duration
				currentBuild.result = result
				currentBuild.builtOnMachineName = builtOnMachineName
				currentBuild.timestamp = new Date(timeStamp)
				currentBuild.buildNumber = buildNumber
				currentBuild.failCount = 0
				currentBuild.skipCount = 0
				currentBuild.passCount = 0
				currentBuild.gitSHA1 = gitSHA1
				currentBuild.gitBranchName = gitBranchName
				currentBuild.buildUrl = buildUrl
				
				def tmp = []
				tmp.addAll(currentBuild.testClasses)

				if(tmp) {
					tmp.each{ TestClass tc ->
						currentBuild.removeFromTestClasses(tc)
						tc.delete()
					}
					
				}

				currentBuild.save()
				loggerService.logError("Build has already been processed in job, overwrite old build")
				return currentBuild
			}
		}

		Build build = new Build(
				user: user,
				duration:  duration,
				result:  result,
				builtOnMachineName:  builtOnMachineName,
				timestamp:  new Date(timeStamp),
				buildNumber: buildNumber,
				failCount: 0,
				skipCount: 0,
				passCount: 0,
				gitSHA1: gitSHA1,
				gitBranchName: gitBranchName,
				buildUrl: buildUrl
				
				)

		if(!build.save()) {
			loggerService.logError("Error creating build")
			return null
		}
		return(build)
	}
}

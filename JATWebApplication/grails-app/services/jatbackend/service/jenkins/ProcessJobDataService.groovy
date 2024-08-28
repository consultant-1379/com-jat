package jatbackend.service.jenkins

import grails.transaction.Transactional
import jatbackend.Interfaces.jenkins.IJob
import jatbackend.domain.Job
import jatbackend.groovysource.JobType

import org.codehaus.groovy.grails.web.json.JSONArray

@Transactional
/**
 * Used for processing the job data, implements the IJob interface
 */
class ProcessJobDataService implements IJob {

    def jobFactoryService
    def loggerService
    /**
     * Processes the job data
     * @param input The JSON input data
     * @return Job returns a job if created from the JobFactory
     */
    Job processJobData(def input) {
        if(!input) {
            loggerService.logError("Input to processJobDataService is null")
            return null
        }

        String jobName = null
        String childJobName = null
        JobType jobType
        String upstreamProject = null
        JSONArray actions = input.actions

        if(!input.actions){
            loggerService.logError("Input action field missing for job data")
            return null
        }

        for(int i = 0; i < actions.size(); i++){
            if(!actions[i].getClass().toString().toLowerCase().contains("null")){
                //Can not be checked in other way
                if(actions[i].causes){
                    for(int j = 0; j < (actions[i].causes).size(); j++){
                        if(actions[i].causes[j].upstreamProject){
                            upstreamProject = actions[i].causes[j].upstreamProject
                        }
                    }
                }
            }
        }

        if(!upstreamProject.equals(null)) {
            jobType = JobType.MATRIX
            jobName = upstreamProject

            if(input.fullDisplayName.lastIndexOf("#") > 0) {
                //If fulldisplayname contains build number, remove, also remove unicode char >
                childJobName = input.fullDisplayName.substring(0,
                        input.fullDisplayName.lastIndexOf("#")-1).replace(" \u00bb ", " ")
            } else {
                childJobName = input.fullDisplayName.replace(" \u00bb ", " ")
            }
        } else {
            jobType = JobType.SINGLETON
            jobName = input.fullDisplayName.split(" ")[0]
        }

        return(jobFactoryService.produce(jobType, [jobName, childJobName]))
    }
}


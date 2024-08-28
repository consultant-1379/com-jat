package jatbackend.service.jenkins

import grails.transaction.Transactional
import groovy.sql.Sql
import jatbackend.Interfaces.jenkins.IJobFactory
import jatbackend.domain.Job
import jatbackend.groovysource.JobType

import java.sql.SQLException

@Transactional
/**
 * Constructs the job, this class implements the IJobFactory Interface
 */
class JobFactoryService implements IJobFactory {

    def dataSource
    def loggerService

    /**
     * Used for producing jobs
     * @param jobNames The job names
     * @param jobType The job type, enum type
     * @return Job produces a job instance
     */
    public Job produce(JobType jobType, ArrayList<String> jobNameList) {
        if(!JobType.values().contains(jobType)) {
            loggerService.logError("Specified job type is not defined as a job type")
            return null
        }

        if(jobNameList.size() == 0) {
            loggerService.logError("jobNameList can not be empty")
            return null
        }

        switch(jobType) {
            case JobType.MATRIX:

                if(jobNameList.size() != 2) {
                    loggerService.logError("jobNameList does not contain two job names")
                    return null
                }

                def sql = new Sql(dataSource)
                Job job = null
                String sqlInsertQuery = "INSERT IGNORE INTO job values('" + jobNameList[0] + "','" + jobType + "');"

                try {
                    sql.execute(sqlInsertQuery)
                    sql.close()
                } catch(SQLException e) {
                    loggerService.logError("Error creating job: SQLException: " + e)
                    sql.close()
                    return null
                }

                job = Job.get(jobNameList[0])
                if(!job) {
                    loggerService.logError("Error fetching master job")
                    return null
                }
                Job childJob = Job.findOrSaveByJobNameAndJobType(jobNameList[1], JobType.CHILD)
                if(!childJob) {
                    loggerService.logError("Error fetching child job")
                    return null
                }

                if(!job.addToChildJobs(childJob)) {
                    loggerService.logError("Error bindning job to child job")
                    return null
                }

                return childJob

            case JobType.SINGLETON:

                Job job = Job.findOrSaveByJobNameAndJobType(
                jobNameList[0],
                jobType
                )

                if(!job) {
                    loggerService.logError("Error creating singleton job")
                    return null
                }

                return job

            default:
                loggerService.logError("No job type was specified in jobFactoryService")
                return null
        }
    }
}

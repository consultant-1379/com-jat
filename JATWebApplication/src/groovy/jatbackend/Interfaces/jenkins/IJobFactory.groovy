package jatbackend.Interfaces.jenkins

import jatbackend.domain.Job
import jatbackend.groovysource.JobType

/**
 * Defines the Job Factory interface
 */
public interface IJobFactory {

    /**
     * The produce function creates a job instance, with the specified input parameters.
     * The jobType can be selected from one of the finite enumeration values in JobType.
     *
     * The ArrayList consist of all the names that should be processed in the factory.
     * E.g. if a matrix job, then the ArrayList will look like: ["MatrixjobName", "childJob"]
     *
     * @param jobNames The job names
     * @param jobType The job type, enum type
     * @return Job returns a job if created
     */
    Job produce(JobType jobType, ArrayList<String> jobNameList)
}
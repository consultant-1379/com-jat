package jatbackend.Interfaces.jenkins

import jatbackend.domain.Job

/**
 * Defines the job interface
 */
public interface IJob {

    /**
     * The processJobData function parses arbitrary input and passes this to the JobFactoryService.
     * It parses the content from the input, e.g. JSON attributes, and creates a job out of this.
     *
     * @param input The data input to be processed
     * @return Job returns a job
     */
    Job processJobData(def input)
}
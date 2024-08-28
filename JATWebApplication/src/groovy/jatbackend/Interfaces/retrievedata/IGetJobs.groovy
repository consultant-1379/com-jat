package jatbackend.Interfaces.retrievedata;

/**
 * This interface contains methods for retrieving Jobs of different types.
 */
public interface IGetJobs {

    /**
     * getJobs is used to retrieve all top-level jobs
     *
     * @return List containing all top-level jobs and all their attributes
     */
    List getJobs();

    /**
     * getChildJobs is used to retrieve all jobs within another job
     *
     * @param jobId Identifier that is used for retrieval of a job
     * @return List containing all jobs within the job with jobId
     */
    List getChildJobs(String jobId);

    /**
     * buildsInJob is used to retrieve all builds in a
     * job, identifying the job by its id
     *
     * @param jobId Identifier that is used for retrieval of a job
     * @return List containing all builds within the job with jobId
     */
    List buildsInJob(String jobId);
}
package jatbackend.Interfaces.retrievedata;

/**
 * This interface contains methods for retrieving calculated sums among
 * objects of the same domain.
 */
public interface IGetTotalCounts {

    /**
     * getTotalCounts is used to sum one or several properties
     * among the builds in a job
     *
     * @return List containing the job name and the calculated sums
     * of the specified properties
     */
    List getTotalCounts();
}
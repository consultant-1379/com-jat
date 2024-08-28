package jatbackend.Interfaces.retrievedata;

import jatbackend.domain.Build

/**
 * This interface contains methods for retrieving Builds that fulfill
 * specified criterias.
 */
public interface IGetBuilds {

    /**
     * recentBuildsInJobs is used to retrieve the most recent builds with
     * certain defined criterias in all Singleton and Matrix jobs.
     *
     * @return List containing all Singleton and Matrix job and
     * their most recent builds
     */
    List recentBuildsInJobs();

    /**
     * showBuild is used to retrieve a build by using its id
     *
     * @param buildId Identifier that is used for retrieval of a build
     * @return Build with the identifier buildId
     */
    Build showBuild(String buildId);
}
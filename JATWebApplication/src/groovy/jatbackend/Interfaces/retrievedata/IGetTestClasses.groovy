package jatbackend.Interfaces.retrievedata;

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * This interface contains methods that are used for retrieving TestClasses.
 */
public interface IGetTestClasses {

    /**
     * getTestClassesWithStatus is used to retrieve all testclasses
     * with certain statuses
     *
     * @param buildId Identifier that is used for retrieval of a build
     * @return JSONObject containing all TestClasses with the specified statuses
     */
    JSONObject getTestClassesWithStatus(String buildId);
}
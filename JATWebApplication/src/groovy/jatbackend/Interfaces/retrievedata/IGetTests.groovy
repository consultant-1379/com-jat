package jatbackend.Interfaces.retrievedata;

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * This interface contains methods for retrieving Tests that
 * fulfill specified criterias.
 */
public interface IGetTests {

    /**
     * getTests is used to retrieve all tests in a testclass that is within a build
     *
     * @param testClassId Identifier that is used for retrieval of a testclass
     * @param buildId Identifier that is used for retrieval of a build
     * @return JSONObject containing all tests in the testclass with testClassId
     */
    JSONObject getTests(String testClassId, String buildId);
}
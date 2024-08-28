package jatbackend.Interfaces.retrievedata;

/**
 * This interface contains methods that are used for
 * retrieving domain objects of the same type that are comparable.
 */
public interface ICompare {

    /**
     * compareTests is used to retrieve tests with the same name and
     * with some data that is comparable.
     * @param testId    Identifier that is used to retrieve its test to
     * determine what tests should be compared
     * @return  List with the tests and what build that contains each test
     */
    List compareTests(String testId, String jobId);
}
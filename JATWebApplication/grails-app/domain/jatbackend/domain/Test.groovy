package jatbackend.domain

/**
 * The test table created in the database
 */
class Test {

    /**
     * The test name, e.g: TC_INTEGRATION_InstallCom_001
     */
    String testName

    /**
     * The error stacktrace for the test, type: Longtext in MYSQL: Max value: 2^32 - 1
     */
    String errorStackTrace

    /**
     * The duration of the test
     */
    int duration

    /**
     * Milliseconds since the test failed
     */
    int failedSince

    /**
     * Test status, could be: PASSED, FAILURE or SKIPPED
     */
    String status

    /**
     * Table properties
     */
    static mapping = {
        errorStackTrace type: 'text'
        version false
    }

    /**
     Attribute constraints
     */
    static constraints = {
        errorStackTrace nullable:true, type: "text"
    }
}
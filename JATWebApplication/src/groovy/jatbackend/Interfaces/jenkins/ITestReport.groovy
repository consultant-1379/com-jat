package jatbackend.Interfaces.jenkins

import jatbackend.domain.Build

/**
 * Defines the Test Report interface
 */
public interface ITestReport {

    /**
     * The function ProcessTestReportData parses the test report data
     * and returns true if the test report was created. The test report
     * consist of a test class and a test.
     * The test is binded to the testClass, and the testClass is binded
     * to the build.
     *
     * @param testReport The test report data to process
     * @param build The build used to bind the created test
     * @return boolean returns true if test report is created
     */
    boolean processTestReportData(def testReport, Build build)
}
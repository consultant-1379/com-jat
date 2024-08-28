package jatbackend.Interfaces.jenkins

import jatbackend.domain.Build
import jatbackend.domain.Job

/**
 * Defines the build interface
 */
public interface IBuild {

    /**
     * The processBuildData takes arbitrary input as parameter,
     * and process it in order to produce a build instance.
     * The input is parsed and put into variables, before passed to new Build()
     * @param input The data input to be processed
     * @return Build returns a build
     */
    Build processBuildData(def input, Job job)
}
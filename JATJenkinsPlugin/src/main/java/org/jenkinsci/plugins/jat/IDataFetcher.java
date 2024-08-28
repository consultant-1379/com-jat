package org.jenkinsci.plugins.jat;

import java.util.ArrayList;

/**
 * The IDataFetcher constructs the interface between the
 * perform method in the JAT post-build action plugin
 * and the Jenkins build and/or test data. Every piece of
 * data that should be fetched and used by the JAT must
 * implement the IDataFetcher. The role of the IDataFetcher
 * is to retrieve Jenkins build data and wrap it into some
 * object structure that can be dispatched by an IDataDispacther.
 */
public interface IDataFetcher {

    /**
     * Fetches Jenkins build and/or test data from the Jenkins APi.
     * @return The Object containing the build data retrieved.
     */
    public Object fetch() throws Exception;

    /**
     * Recovers locally persisted data.
     * @return An ArrayList containing the IPersistedDataObject(s)
     * that is/are retrieved from locally persisted data.
     */
    public ArrayList<IPersistedDataObject> recover() throws Exception;

}

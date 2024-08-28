package org.jenkinsci.plugins.jat;

/**
 * The IPersistedDataObject serves as a wrapper for any
 * persistable piece of Jenkins build and/or test data
 * in the JAT. The role of the Object is to ensure that
 * a persisted Object is removed locally if and only if
 * a dispatcher successfully transmits the data to the
 * JAT web application back end.
 */
public interface IPersistedDataObject {

    /**
     * @return the Object containing the Jenkins build and/or test data
     */
    public Object getObject();

    /**
     * Removes the locally stored file containing the
     * Jenkins build and/or test data.
     */
    public void remove() throws Exception;
}

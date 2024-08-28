package org.jenkinsci.plugins.jat;

/**
 * The IDataDispatcher constructs the interface between the
 * perform method in the JAT post-build action plugin
 * and the JAT web application back end. The role of
 * the IDataDispatcher is to forward Jenkins data to the JAT
 * web application back end.
 */
public interface IDataDispatcher {


    /**
     * Dispatches the Jenkins data wrapped in the IDataDispatcher
     * object to the JAT web application back end.
     * @throws DispatchException when dispatch to JAT web
     * application backend is not possible.
     */
    public void dispatch() throws DispatchException;

    /**
     * Persists the data of the IDataDispatcher locally.
     * This method is invoked in JAT.java when the IDataDispatcher
     * fails to dispatch Jenkins build and/or test data
     * to the JAT web application back end and the User
     * have configured the plugin with persistent session.
     * @throws PersistException when the plugin fails to persist
     * Jenkins build and/or test data locally.
     */
    public void persist() throws PersistException;
}


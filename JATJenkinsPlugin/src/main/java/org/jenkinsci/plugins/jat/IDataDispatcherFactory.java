package org.jenkinsci.plugins.jat;

import hudson.model.BuildListener;

import org.json.simple.JSONObject;

/**
 * The IDataDispathcerFactory is used to generate an
 * IDataDispatcher for the JAT.
 */
public class IDataDispatcherFactory {

    public enum Dispatcher
    {
        JSON /* JSONDataDispatcher */
    };

    /**
     * Fethches the desired IDataDispatcher that is used for dispatching
     * Jenkins build and/or test data to the JAT web application back end.
     * @param dataDispatcherType int designating the IDataDispatcher type.
     * @param obj Object containing some arbitrary data  that can be  utilized
     * in the selectet IDataFetcher type.
     * @param backendURLMapping absolute URL pointing to the JAT web application
     * backend. Given as a String.
     * @param shouldPersist boolean flag indicating if the data wrapped in the object
     * should be persisted locally should HTTP forward to the JAT backend fail.
     * @param listener BuildListener used for logging purposes.
     * @return the IDataDispatcher.
     */
    public static IDataDispatcher getDataDispatcher(Dispatcher dataDispatcherType,
                                                    Object obj,
                                                    String backendURLMapping,
                                                    BuildListener listener) {

        if(listener == null) {
            /* Don't create Dispatcher if listsner == null */
            System.out.println("Unable to create IDataDispatcher due to corrupted BuildListener: null");
            return null;
        }

        if(obj == null) {
            /* Don't create Dispatcher if obj == null */
            listener.getLogger().println("Unable to create IDataDispatcher due to null object");
            return null;
        }

        URLValidator validator = new URLValidator(backendURLMapping);
        if(!validator.isValidURL()) {
            /* Don't create Dispatcher if backendURLMapping is invalid */
            listener.getLogger().println("Unable to create IDataDispatcher: "
                                         + backendURLMapping
                                         + " is not a valid URL");
            return null;
        }

        switch(dataDispatcherType) {
            case JSON : {
                return new JSONDataDispatcher((JSONObject) obj,
                                              backendURLMapping);
            }
            default: {
                listener.getLogger().println("Invalid IDataDispatcher type: "
                                             + dataDispatcherType
                                             + ". JAT post-build action halted");
                return null;
            }
        }
    }
}

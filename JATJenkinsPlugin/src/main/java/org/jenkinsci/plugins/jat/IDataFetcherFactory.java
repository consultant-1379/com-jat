package org.jenkinsci.plugins.jat;

import hudson.model.BuildListener;

/**
 * The IDataFetcherFactory is used to generate an
 * IDataFetcher for the JAT.
 */
public class IDataFetcherFactory {

    public enum Fetcher
    {
        JSON /* JSONDataFetcher */
    };

    /**
     * Fethches the desired IDataFetcher that can be used for fetching
     * Jenkins Build and Test data to the a JAT IDataDispatcher.
     * @param dataFetcherType int designating the IDataFetcher type.
     * @param jenkinsBuildURLMapping absolute URL pointing to the jenkins build.
     * Given as a String.
     * @param listener BuildListener used for logging purposes.
     * @return
     */
    public static IDataFetcher getDataFetcher(Fetcher dataFetcherType,
                                              String  jenkinsBuildURLMapping,
                                              BuildListener listener) {

        if(listener == null) {
            System.out.println("Unable to create IDataDispatcher due to corrupted BuildListener: null");
            return null;
        }

        URLValidator validator = new URLValidator(jenkinsBuildURLMapping);
        if(!validator.isValidURL()) {
            /* Don't create Dispatcher if backendURLMapping is invalid */
            listener.getLogger().println("Unable to create IDataDispatcher: "
                                         + jenkinsBuildURLMapping
                                         + " is not a valid URL");
            return null;
        }

        switch(dataFetcherType) {
            case JSON : {
                return new JSONDataFetcher(jenkinsBuildURLMapping);
            }
            default: {
                listener.getLogger().println("Invalid IDataFetcher type: "
                                             + dataFetcherType
                                             + ". JAT post-build action halted");
                return null;
            }
        }
    }
}

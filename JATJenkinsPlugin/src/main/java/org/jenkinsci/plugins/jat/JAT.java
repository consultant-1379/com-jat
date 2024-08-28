package org.jenkinsci.plugins.jat;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.ArrayList;

import jenkins.model.JenkinsLocationConfiguration;

import org.jenkinsci.plugins.jat.IDataDispatcherFactory.Dispatcher;
import org.jenkinsci.plugins.jat.IDataFetcherFactory.Fetcher;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * JAT Jenkins plug-in main class. Extends the Notifier
 * object to operate as a post-build action. The JAT
 * invokes the IDataFetcher and the IDataDispatcher for
 * retrieving and sending Jenkins build/test data.
 */
public class JAT extends Notifier {

    private String applicationURL;
    private boolean isPersistedSession;
    private String globalJenkinsURL = new JenkinsLocationConfiguration().getUrl();

    /**
     * Constructor for the JAT Object. Instance data is set by
     * fetching the JAT web application URL and boolean flag for
     * persisted sessions from the JatDescriptor associated with
     * the object.
     */
    @DataBoundConstructor
    public JAT() {
        this.applicationURL = getDescriptor().getApplicationURL();
        this.isPersistedSession = getDescriptor().isPersistedSession();
    }

    @Override
    public JatDescriptor getDescriptor() {
        return (JatDescriptor) super.getDescriptor();
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    /**
     * @return the absolute URL pointing to the JAT web application back end
     */
    public String getApplicationURL() {
        return this.applicationURL;
    }

    /**
     * @return the boolean flag associated with this object
     */
    public boolean isPersistedSession() {
        return this.isPersistedSession;
    }

    /**
     * Fetches Jenkins build and test data by invoking the fetch()
     * method on the given IDataFetcher
     * @param dataFetcher the IDataFetcher used to fetch Jenkins data.
     * @param listener the BuildListener used for logging purposes.
     * @return The Object containing the Jenkins build data wrapped into
     * the form designated by the given IDataDispatcher type.
     */
    private Object fecthJenkinsBuildData(IDataFetcher dataFetcher, BuildListener listener) {
        Object collectedData = null;
        try {
            collectedData = dataFetcher.fetch();
        } catch(Exception e) {
            listener.getLogger().println(e.getMessage());
        }
        return collectedData;
    }

    /**
     * Dispatches retreived Jenkins data to the JAT web application
     * back end by invoking the dispatch() method on the IDataDispatcher.
     * If dispatching is not possible, data will be persisted locally by
     * invoking the persist() method on the IDataDispatcher Object.
     * @param dataDispatcher the IDataDispatcher used to dispatch Jenkins data.
     * @param listener the BuildListener used for logging purposes.
     * @return a boolean variable indicating the result of the dispatching. True
     * if data was successfully dispatched to the JAT web application back end or
     * if data was successfully persisted locally, else false.
     */
    private boolean dispatchJenkinsBuildData(IDataDispatcher dataDispatcher,
                                             BuildListener listener) {

        try {
            /* Dispatch collected Jenkins data to JAT web application back end */
            dataDispatcher.dispatch();
        } catch(DispatchException dispatchException) {
            if(this.isPersistedSession) {
                try{
                    /* Persist Jenkins build data locally if dispatching fails */
                    dataDispatcher.persist();
                } catch(PersistException persistException) {
                    listener.getLogger().println(persistException);
                    return false;
                }
                listener.getLogger().println("Data was persisted locally "
                        + " after dispatcher failing to dispatch data to "
                        + "JAT web application back end\n"
                        + dispatchException);
                return true;
            }
            listener.getLogger().println(dispatchException);
            return false;
        }
        listener.getLogger().println("JAT completed collecting "
                + "and dispatching Jenkins build and test data successfully!");
        return true;
    }

    /**
     * Recovers locally persisted Jenkins build and/or test data by
     * invoking recover() on the IDataFecther Object and dispatches
     * each recovered Object to the JAT web application back end.
     * When an Object have been successfully transmitted to the JAT
     * web application, the locally persisted file containing the
     * Jenkins data is removed.
     * @param dataFetcher dataFetcher the IDataFetcher used to fetch Jenkins data.
     * @param listener the BuildListener used for logging purposes.
     * @return a boolean variable indicating if the recovery and dispatching
     * of all locally persisted data was performed successfully. True if
     * successful else false.
     */
    private boolean recoverAndDispatchPersistedData(IDataFetcher dataFetcher, BuildListener listener) {

        ArrayList<IPersistedDataObject> recoveredData = null;

        try {
            recoveredData = dataFetcher.recover();
        } catch(Exception e) {
            listener.getLogger().println(e.getMessage());
        }

        if(recoveredData != null) {
            for(int i = 0; i < recoveredData.size(); i++) {
                IDataDispatcher dataDispatcherForRecoveredData =
                        IDataDispatcherFactory.getDataDispatcher(Dispatcher.JSON,
                                recoveredData.get(i).getObject(),
                                this.applicationURL,
                                listener);

                if(dataDispatcherForRecoveredData == null) {
                    return false;
                }

                try {
                    dataDispatcherForRecoveredData.dispatch();
                } catch(DispatchException e) {
                    listener.getLogger().println(e);
                    return false;
                }

                try {
                    recoveredData.get(i).remove();
                } catch(Exception e) {
                    listener.getLogger().println(e);
                }
            }
        }
        listener.getLogger().println("JAT completed dispacthing "
                + " locally persisted Jenkins build and test successfully!");
        return true;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
            BuildListener listener) throws InterruptedException, IOException {

        if(listener == null || build == null) {
            return false;
        }
        /* Regular expression for obtaining a valid URL */
        String urlString =  globalJenkinsURL + build.getUrl().replaceAll("\\.\\/", "");

        /* Construct fetcher object and fetch Jenkins build and test report */
        IDataFetcher dataFetcher = IDataFetcherFactory.getDataFetcher(Fetcher.JSON,
                                                                      urlString,
                                                                      listener);
        if(dataFetcher == null) {
            return false;
        }

        Object collectedData = fecthJenkinsBuildData(dataFetcher, listener);
        if(collectedData == null) {
            return false;
        }

        if(this.isPersistedSession) {
            recoverAndDispatchPersistedData(dataFetcher, listener);
        }
        /* Construct dispatcher object and dispatch data */
        IDataDispatcher dataDispatcher =
                IDataDispatcherFactory.getDataDispatcher(Dispatcher.JSON,
                                                         collectedData,
                                                         this.applicationURL,
                                                         listener);
        if(dataDispatcher == null) {
            return false;
        }

        if(!dispatchJenkinsBuildData(dataDispatcher, listener)) {
            return false;
        }
        return true;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Extension
    public static class JatDescriptor extends BuildStepDescriptor<Publisher> {

        private String applicationURL;
        private boolean isPersistedSession;

        /**
         * @return the absolute URL pointing to the JAT web application back end
         */
        public String getApplicationURL() {
            return this.applicationURL;
        }

        /**
         * @return the boolean flag associated with this object
         */
        public boolean isPersistedSession() {
            return this.isPersistedSession;
        }

        /**
         * Sets the application URL of the Object to applicationURL.
         * @param applicationURL the absolute URL pointing to the JAT web
         * application back end
         */
        public void setApplicationURL(String applicationURL) {
            this.applicationURL = applicationURL;
        }

        /**
         * Sets the boolean flag persisted session to persistedSession
         * @param persistedSession the boolean flag indicating if data
         * should be persisted locally in the event of unsuccessful forwarding.
         * True if persisted session is activated, else false.
         */
        public void setPersistedSession(boolean persistedSession) {
            this.isPersistedSession = persistedSession;
        }

        public JatDescriptor() {
            super(JAT.class);
            load();
        }

        @Override
        public String getDisplayName() {
            return "Jenkins Statistical Test Analysis Tool";
        }

        @Override
        public boolean configure(StaplerRequest req, net.sf.json.JSONObject json) throws FormException {
            setApplicationURL((String) json.get("applicationURL"));
            setPersistedSession(json.getBoolean("isPersistedSession"));
            save();
            return true;
        }

        @Override
        public boolean isApplicable(@SuppressWarnings("rawtypes")
        Class<? extends AbstractProject> arg0) {
            return true;
        }

        /**
         * Asserts that the given input URL for the JAT web application is a valid URL.
         * @param applicationURL Absolute URL pointing to the JAT web application back end.
         * @return Formvalidation Message
         */
        public FormValidation doCheckApplicationURL(@QueryParameter String applicationURL) {
            URLValidator url = new URLValidator(applicationURL);
            if(url.isValidURL()) {
                return FormValidation.ok();
            } else {
                return FormValidation.error(applicationURL
                        + " is not a valid URL, unable to establish connection");
            }
        }
    }
}
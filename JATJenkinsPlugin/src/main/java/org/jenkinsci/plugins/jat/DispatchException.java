package org.jenkinsci.plugins.jat;

/**
 * The DispatchException should be thrown by
 * the dispach() method in any IDataDispatcher
 * when the method fails to dispatch the Jenkins
 * build and/or test data to the JAT web application
 * back end. The exception is handled in JAT.java
 * by invoking the persist() method on the
 * IDataDispatcher Object if the plugin is configured
 * with the persisted session option.
 */
@SuppressWarnings("serial")
public class DispatchException extends Exception {

    public DispatchException(String msg){
        super(msg);
     }
}

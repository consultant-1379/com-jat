package org.jenkinsci.plugins.jat;

/**
 * The PersistException should thrown by the
 * persist() method in any IDataDispatcher when
 * the method fails to persist Jenkins build and/or
 * test data locally. The exception is then handled
 * in JAT.java
 */
@SuppressWarnings("serial")
public class PersistException extends Exception {

    public PersistException(String msg){
        super(msg);
    }
}

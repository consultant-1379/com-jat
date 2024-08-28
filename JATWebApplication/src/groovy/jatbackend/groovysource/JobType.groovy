package jatbackend.groovysource;

/** Used for defining the different job types */
public enum JobType {
    // Definition of the job type enumeration
    UNKNOWN(0), MATRIX(1), SINGLETON(2), CHILD(3)

    private int value;

    private JobType(int value) {
        this.value = value;
    }

}
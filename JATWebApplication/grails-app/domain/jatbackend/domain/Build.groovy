package jatbackend.domain

/**
 * The build table created in the database
 */
class Build {

    //Build specific variables

    /**
     * The buildNumber is the build id that comes from the Jenkins PB-plugin
     */
    int buildNumber

    /**
     *The duration of the build
     */
    int duration

    /**
     * When the build was triggered, Example: YYYY-MM-DD HH:MM:SS, e.g: 2015-07-10 11:35:55
     * The timestamp sent from the PB-plugin is in Epoch time, e.g: 1435735237000,
     * then converted to Date object: new Date(1435735237000)
     */
    Date timestamp

    /**
     * The user that executed the build, can be null
     */
    String user

    /**
     * Can either be SUCCESS or FAILURE, recieved as a String from the Jenkins API
     */
    String result

    /**
     * The machine name that the build was run on, can be null
     */
    String builtOnMachineName // builtOnMachineName is a Jenkins specified parameter: built on machine name

    //Test results
    /**
     * Total test failures count, total count for all tests executed within the build
     */
    int failCount

    /**
     * Total test skip count, total count for all tests executed within the build
     */
    int skipCount

    /**
     * Total test pass count, total count for all tests executed within the build
     */
    int passCount

    /**
     * SHA1 for the git commit
     */
    String gitSHA1

    /**
     * In which branch the commit belongs to
     */
    String gitBranchName

	String buildUrl
	
	
    /**
     *Declaration that a build has many test classes, creates a database relation to this
     */
    static hasMany = [testClasses : TestClass]

    /**
     * Table properties
     */
    static mapping = {
		testClasses cascade: "all-delete-orphan"
		version false
		}

    /**
     * Attribute constraints
     */
    static constraints = {
        builtOnMachineName nullable: true, blank: true
        user nullable: true, blank: true
        gitSHA1 nullable: true
        gitBranchName nullable: true
		buildUrl nullable: true
    }
}
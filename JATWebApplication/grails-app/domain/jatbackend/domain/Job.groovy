package jatbackend.domain

/**
 * The job table created in the database
 */
class Job {

	/**
	 * The job name, e.g: COM-com_dev-LSB64-Build
	 */
	String jobName

	/**
	 * The job type, could be extended in the enumeration JobType, in package jatbackend.groovysource. Saved as String
	 */
	String jobType

	/**
	 * Declaration that a job has many childjobs and builds, creates a database relation to these
	 */
	static hasMany = [childJobs : Job, builds : Build]

	/**
	 * Table properties
	 */
	static mapping = {
		version false
		id generator: 'assigned', name: 'jobName'
		childJobs cascade: "all-delete-orphan"
		builds cascade: "all-delete-orphan"
	}

	/**
	 * Attribute constraints
	 */
	static constraints = {
		jobName unique: true, nullable: false
		jobType nullable: false
	}
}

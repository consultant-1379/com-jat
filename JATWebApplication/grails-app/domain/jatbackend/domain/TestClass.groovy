package jatbackend.domain

/**
 * The "test class" table created in the database
 */
class TestClass {

	/**
	 * Test class name, e.g: se.ericsson.jcat.com.integration.testcases.TestSmIntegration
	 */
	String className

	/**
	 * Status of a test class, e.g. passed
	 */
	String status

	/**
	 * Declaration that a test class has many tests, creates a database relation to this
	 */
	static hasMany = [tests : Test]

	/**
	 * Table properties
	 */
	static mapping = {
		tests cascade: "all-delete-orphan"
		version false
	}

	/**
	 * Attribute constraints
	 */
	static constraints = {
		status nullable: true
	}
}

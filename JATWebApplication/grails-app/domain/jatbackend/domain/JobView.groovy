package jatbackend.domain

class JobView {

	String viewName
	
	static hasMany = [jobs: Job]
	
	static mapping = {
		version false
	}
    static constraints = {
		
    }
}

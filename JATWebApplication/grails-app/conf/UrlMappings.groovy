/** UrlMappings define the API endpoints for the web application */
class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{ constraints { // apply constraints here
            } }

        //VIEWS
        "/"(view:"/index")
        "/index.htm"(view:"/home")
        "/settings.htm"(view:"/settings")
        "/statistics.htm"(view:"/statistics")
        "/build.htm"(view:"/build")
        "/matrixjob.htm"(view:"/matrixjob")
        "/singletonjob.htm"(view:"/singletonjob")
        "/userguide.htm"(view:"/userguide")
        "/testresults.htm"(view:"testresults")

        //LOGIC

        "/jenkins"(controller:"jenkins", action:"run")

        "/job"(resources: 'job'){
            "/job"(resources: 'job')
            "/build"(resources: 'build')
        }

        "/build/$buildid/testclass/"(controller:"testClass", action: "getTestClasses")
        "/build/$buildid/testclass/$testclassid/test"(controller:"test", action: "getTests")
        "/getjobwithbuilds"(controller:"build", action:"getJobWithBuilds")
        "/showbuild/$buildid/"(controller:"build", action: "showBuild")
        "/gettotalcounts"(controller:"build", action:"getTotalCounts")
        "/gettestclasses/$buildid"(controller:"testClass", action: "getTestClasses")
        "/testclass/$testid/build/$buildid"(controller: "test", action: "getTests")
        "/CHILD/$jobId/compareTests/$testid"(controller: "test", action: "compare")
        "/getrecentbuildinchildjobs/$jobId"(controller: "build", action: "getRecentBuildInChildJobs")

		"/jobview"(resources: 'jobView')
		"/getjobviews/$id"(controller: "jobView", action: "jobviews")
		"/addjob"(controller: "jobView", action: "addjob")
		
        "500"(view:'/error')
    }
}

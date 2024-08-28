
The Jenkins Statistical Test Analysis Tool
******************************************
* By: Morhaf Alaraj,
*     Viktor Bostrand and
*     Andres Tuomela Vazques
*
* Licence: Ericsson
******************************************

This document contains the following
------------------------------------

 * INTRODUCTION
 * DESIGN
 * REQUIREMENTS & GGTS SETUP
 * RECOMMENDED MODULES AND TRICKS
 * BUILDING
 * INSTALLATION
 * CONFIGURATION

 For architecture, system design and more detail
 see technical document.

INTRODUCTION
------------
Continuous integration (CI) is an important part of Ericsson’s
software infrastructure. CI gives developers the ability to set
up and monitor complex build chains that are essential for the
production line. However, Jenkins, the tool at hand, lacks certain
desirable features and properties that could enhance the CI system
and give developers a more intuitive way of visualizing job executions.
The Jenkins test statistic collector and analysis tool (JAT) is a tool
that provides Ericsson developers with a flexible and straightforward way
of monitoring the results of Jenkins job executions.

The JAT combines a Jenkins plugin solution together with a web application
to visualize Jenkins build and test executions in intuitive graphs for a
quick overview but also the ability to dive deep into test execution
specifics for more detail.

DESIGN
------
The back end is written in Grails 2.4.4 and utilizes a MySQL database.
It has two primary functions. The first one is to receive build
data from the Jenkins plugin, process it and finally insert it into the database.
These tables have been set up in grails by creating domain classes where the class
name is the name of the table. To create desired columns in the database tables,
attributes are simply added in the domain classes as variables.The relationship
between two tables is also defined within the domain classes by using e.g. a
hasMany or belongsTo relationship. 

Example domain: Job

class Job {
          String jobName
          String jobType
          static hasMany = [childJobs : Job, builds : Build]
 
          static mapping = {
                      version false
                      id generator: 'assigned', name: 'jobName'
                      childJobs cascade: "all-delete-orphan"
                      builds cascade: "all-delete-orphan"
          }
 
          static constraints = {
                      jobName unique: true, nullable: false
                      jobType nullable: false
          }
}

This class will result in a database table named “Job”, with two attributes.
Two relationship tables will also be created (hasMany relationship).
The relation is between the Job class and itself, but also with the Build class.
The build class is also declared as a domain. The cascade functionality inherits from
the SQL language. It specifies that the child will also be deleted once a parent is deleted.

The other function is to provide information to the presentation layer/front end upon a HTTP
request. This is done by setting up endpoints in the URL mapping configuration in Grails,
so Grails know which controller and method to call when a certain request is performed. 

URLMapping.groovy example

"/jenkins"(controller:"jenkins", action:"run")

This simply defines that incoming requests to the URL “/jenkins” should be forwarded to
the controller “jenkins” and the action/function “run”. For REST API’s, the grails specific
“resources” can be used. This automatically maps HTTP methods towards the correct function. 

"/job"(resources: 'job')

HTTP POST -> save() in controller
HTTP GET -> index() in controller
HTTP PUT -> update() in controller
HTTP DELETE -> delete() in controller

As mentioned earlier, the back end has two primary functions. One part handles the data
processing from the post build plugin and insertion to the database, while the other part
handles the data delivery to the front end upon request.

The main entry point for the web application is the JenkinsController. It fetches the
JSON data that is delivered from the post build plugin and delegates it to different
Grails services (business logic handlers) for handling the data.



The flow in JenkinsController (simplified)

CODE:
Job jobObj = processJobDataService.processJobData(buildJsonData)
//call the processJobDataService and pass the buildJsonData to the
//function in that service that will process the data. This will return the job instance that is created.
Build buildObj = constructBuildService.processBuildData(buildJsonData, jobObj)
//We will pass the buildJsonData together with the job. 
//The result of this call is the build instance, that is used to verify e.g.
//if the build already exist within the job.
job.addToBuilds(buildObj)
//This will bind the build to the job.
testReportService.processTestReportData(testReportJsonData, buildObj)
 //This will create the testreport and bind the build to it.

REQUIREMENTS & GGTS SETUP
----------------------------
The following modules and tools are needed when working with the JAT web
application:

  * Grails 2.4.4 (https://grails.org/download.html)
  * JDK 1.7
  * GGTS, Grails Eclipse environment (https://spring.io/tools/ggts)
  * MySQL

NOTE: Newer versions of Java and Grails may conflict!

After installing the GGTS IDE, import the JAT web application project as
following:

   File > Import > Grails > Grails Project > Path/to/JAT/Web

Happy coding!

When developing it is recommended to work locally in the GGTS environment.
To run locally set the GGTS run configurations to:

  grails > run-app

This will run the application locally.
NOTE: Make sure to set the variable JATSERVER = "http://localhost/" in:

  assets > javascripts > angularinit.js

RECOMMENDED MODULES & TRICKS
----------------------------
Sometimes GTTS shows some mystic errors in the project and the following command
usually does the trick to remove these (managed in the run configuration):

  grails > clean

NOTE: It is recommended to perform a 'clean' for the project before generating the
*.war file used for deploying.

BUILDING
--------
To build the application for deployment is done from the command line with
the following command:

  $ grails war

This will generate a *.war file in /target/. This *war-file should then
be renamed to ROOT.war and can then be uploaded to the JAT web server (see
section Installation).

NOTE: When building the JAT web application make sure to set the
variable JATSERVER = "http://10.64.87.159/" in:

  assets > javascripts > angularinit.js

INSTALLATION
------------
To install the web application on the running tomcat server simply shut down
the server from command line:

  $ sudo service tomcat7 stop

Remove the ROOT.war file and the ROOT folder in:

  > var/lib/tomcat7/webapps/
  ($ rm -rf /var/lib/tomcat7/webapps/ROOT*)

Paste the new ROOT.war file (see section Building) in the same directory.
From the command line type:

  $ sudo service tomcat7 start

Browse to http://10.64.87.159/, open a can of beer, enjoy!

CONFIGURATION
-------------
For full documentation of how to manage grails congigurations please visit:

http://grails.github.io/grails-doc/2.4.4/guide/conf.html

The MYSQL configurations are configured whitin GGTS in:

  conf > DataSource.groovy

Here the URL to the MYSQL data source is configured along with the username
and password to the database. The currently used database carry the following
settings for the production environment:

  username = "root"
  password = "mS2yxLsXwd"

In the file:

  conf > UrlMappings.groovy

The URL mappings are configured. Here, you need to add the URL mappings when,
for instance, creating a new html view. See existing mappings and documentation
for additional information.

GRAILS DEPENDENCIES:

conf -> BuildConfig.groovy

It is simple to add new Grails dependencies, example:

    plugins {
        build ":tomcat:7.0.55"
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'
        compile ":asset-pipeline:2.3.9"
        compile ":mysql-connectorj:5.1.22.1"
        runtime ":hibernate4:4.3.6.1"
        runtime ":database-migration:1.4.0"
        runtime ":jquery:1.11.1"
    }

APPLICATION STARTUP INIT:

conf -> Bootstrap.groovy

This file will be executed at application startup or end.

class BootStrap {

  /** init runs before application start */
  def init = { servletContext ->
  }
  /** destroy runs on application shut down */
  def destroy = {
  }
}

It can be good to use when there are some information that always need to be inserted at application startup.
E.g. creating new table rows (User Roles for example). These can be declared inside the init closure.
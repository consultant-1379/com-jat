
The Jenkins Statistical Test Analysis Tool
******************************************
* By: Morhaf Alaraj,
*     Viktor Bostrand and
*     Andres Toumela Vazques
*
* Licence: Ericsson
******************************************

This document contains the following
------------------------------------

 * INTRODUCTION
 * DESIGN
 * REQUIREMENTS & ECLIPSE SETUP
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
The main entry point of the JAT post-build action plugin is the JAT.java. This
file extends the Hudson Notifier class in order to be invoked appropriately as
a post-build action. Wrapped as a nested class within the same file is also the
JATDescriptor (for some reason this seems to be the convention when writing
Jenkins plugins). The descriptor is used to fetch the global configurations from
Jenkins (see section Configuration) and provide the outermost class (the JAT) with
this information.

At the heart of the application is the perform()-method. This is the method that
handles all logic when the post-build action plugin kicks in during the post-build
phase of the Jenkins job execution cycle. Essentially, it consists of two parts
a data fetching phase and a data sending phase that utilizes a IDataFetcher and
an IDataDispatcher respectively. The role of the fetcher is to fetch Jenkins
build and test execution data from the Jenkins JSON API. The role of the sender
is to forward the collected data to the JAT web application, where it can be
persisted and presented.

The JSONDataFetcher is implented as an IDataFetcher, that is the interface
towards the JAT.java main class. The IDataFetcher interface implements two
methods that are used within the JAT.java, these are fetch() - which is used to
fetch Jenkins build data - and recover() - which is used to recover locally
persisted data.

The JSONDataDispatcher is implemented as a IDataDispatcher that, similar to the
IDataFetcher, is then interface towards the JAT.java main class. This interface
implements two methods thatg are used in the JAT.java, these are dispatch() -
used to dispatch data fetched by some IDataFetcher to the JAT web application -
and recover() - used to recover data that has been locally persisted by some
IDataFechter.

REQUIREMENTS & ECLIPSE SETUP
----------------------------
The JAT Jenkins post-build action plugin requires
the following tools:

  * Maven v. 3.1, or newer (https://maven.apache.org/download.cgi)
  * JDK v. 1.6, or newer
  * Jenkins v. 1.532.2, or newer
  * A running instance of the JAT web application

NOTE: The JAT Jenkins post-build action plugin has only
been tested towards Jenkins v. 1.532.2 (The current version
of COM), newer Jenkins versions may conflict.

When working on the JAT post-build action it is recommended to
do so in some IDE, preferably Eclipse. To do this first install the
Maven 'M2Eclipse' (or M2E, for short) plugin that can be found in
the Eclipde market place or at:

  http://download.eclipse.org/technology/m2e/releases

After the maven plugin is successfully installed import the JAT
project by navigating as following:

  File > Import > Maven > Existing Maven Projects > path/to/JAT

It is recommended tho build and run the plugin from command line (see section
Building), however it is possible to run the application from Eclipse by
modifying the Eclipse run configurations to execute target 'hpi:run'

RECOMMENDED MODULES & TRICKS
----------------------------
To run the JAT Jenkins post-build action plugin locally
the following module can be used:

  * Tomcat 6, or newer

Deploying a Jenkins plugin takes time, heaps of time. So instead
of going through the whole cycle with building (and testing), uninstalling,
shutting down Jenkins/Tomcat, restarting and finally deploying, (see
coming sections for full description) the following command can be
very useful when working on the plugin locally:

    $ mvn hpi:run

or optionally (for skipping test phase):

    $ mvn hpi:run -Dmaven.test.skip=true

This builds the plugin and installs it into a local Jenkins instance
immediately without the need of a Jenkins/Tomcat restart. This Jenkins instance
can then be reached at http://localhost:8080/jenkins.

BUILDING
--------
To build the JAT Jenkins post-build action plugin use
following bash commands:

  $ mvn package
  $ mvn install

These commands will also run the automated test suits
for the plugin in. If you would like to skip tests run:

  $ mvn package -Dmaven.test.skip=true
  $ mvn install -Dmaven.test.skip=true

These commands will produce the Jenkins executable file
JAT.hpi in /target/.

To clean up the repository the following command can be
given:

  $ mvn clean

INSTALLATION
------------
To install the JAT Jenkins post-build action plugin the
project must first be built (see section 'building'). The
plugin is installed in Jenkins by navigating to:

  > Manage Jenkins > Manage Plugins

Here, select the 'Advanced' tab and upload the JAT.hpi file.

NOTE: If one instance of the JAT is already installed in
Jenkins it has to be uninstalled before installing the newer
version. This will require a Jenkins restart.

CONFIGURATION
-------------
The JAT Jenkins post-build action plugin is only configured
globally. This means that all jobs that will utilize the JAT
will carry the same settings.

The settings are managed in Jenkins by navigating to:

  > Manage Jenkins > Configure System

Here, you must provide the plugin with an absloute
URL pointing to the JAT web application receiver (initially
configured to be at http://URL/jenkins).

You also have an option to enable the debug logger that will
print debug information to the Jenkins log when some Jenkins
job is running the JAT post-build action plugin.

The JAT Jenkins post-build action plugin also has a 'pesistent
session'-option. This option is used to persist Jenkins data
locally whenever the plugin fails to dispatch data to the JAT
web application. If such a case occurs, and the JAT plugin is
configured with this option, the plugin will try to dispatch the
locally persisted data to the web application when any other job
is running the JAT.

NOTE: The 'pestisted session'-option has only been tested locally
and in automated unit tests, never in a production environment.

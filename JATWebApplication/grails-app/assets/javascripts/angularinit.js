var app = angular.module("JAT", ['ngRoute','vAccordion']); //Defines the Angular Module

var JATSERVER = "http://10.64.87.159/"
app.config(function($routeProvider) {
	$routeProvider

	// route for the home page
	.when('/', {
		templateUrl : 'index.htm',
		controller  : 'mainController'
	})

	// route for the about page
	.when('/statistics', {
		templateUrl : 'statistics.htm',
		controller  : 'statisticsController'
	})

	// route for the contact page
	.when('/settings', {
		templateUrl : 'settings.htm',
		controller  : 'settingsController'
	})
	
	.when('/userguide', {
		templateUrl : 'userguide.htm',
		controller  : 'userguideController'
	})
	.when('/MATRIX/:matrixid', {
		templateUrl : 'matrixjob.htm',
		controller  : 'matrixjobController'
	})
	.when('/CHILD/:childid', {
		templateUrl : 'build.htm',
		controller  : 'buildController'
	})
	.when('/CHILD/:childid/:masterJobName', {
		templateUrl : 'build.htm',
		controller  : 'buildController'
	})
	.otherwise({
		redirectTo: '/err'
	});;
});









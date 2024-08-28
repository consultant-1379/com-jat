var optionsTestBar = {
		xaxis: {
			tickLength: 0,
			mode: "categories",
			axisLabel: 'Build number',
			axisLabelUseCanvas: true,
			axisLabelFontSizePixels: 18,
			axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
			axisLabelPadding: 20
		},
		yaxis: {
			min: 0,
			minTickSize: 1,
			axisLabel: 'Test duration',
			axisLabelUseCanvas: true,
			axisLabelFontSizePixels: 18,
			axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
			axisLabelPadding: 20
		},
		series: {
			bars: {
				show: true,
				barWidth: (100 / 10 ) / 100
			},
		},

		grid: {
			clickable: true,
			hoverable: true,
			backgroundColor: { colors: ["#FFFFFF", "#E3E5E8"] }
		},
		legend: {
			labelBoxBorderColor: "none",
			show: false,
		}
};



var optionsBar = {
		xaxis: {
			tickLength: 0,
			mode: "categories",
			axisLabel: 'Build number',
			axisLabelUseCanvas: true,
			axisLabelFontSizePixels: 18,
			axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
			axisLabelPadding: 20
		},
		yaxis: {
			min: 0,
			minTickSize: 1,
			axisLabel: 'Build duration (s)',
			axisLabelUseCanvas: true,
			axisLabelFontSizePixels: 18,
			axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
			axisLabelPadding: 20
		},
		series: {
			bars: {
				show: true,
				barWidth: (100 / 10 ) / 100
			},
		},

		grid: {
			clickable: true,
			hoverable: true,
			backgroundColor: { colors: ["#FFFFFF", "#E3E5E8"] }
		},
		legend: {
			labelBoxBorderColor: "none",
			show: false,
		}
};

var optionsTests = {
		xaxis: {
			tickLength: 0,
			mode: "categories",
			axisLabel: 'Build number',
			axisLabelUseCanvas: true,
			axisLabelFontSizePixels: 18,
			axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
			axisLabelPadding: 20
		},
		yaxis: {
			min:0,
			minTickSize: 1,
			axisLabel: 'Number of test cases',
			axisLabelUseCanvas: true,
			axisLabelFontSizePixels: 18,
			axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
			axisLabelPadding: 20
		},

		series: {
			stack: true,
			bars: {
				show: true,
				barWidth: (100 / 10 ) / 100
			},
		},

		grid: {
			clickable: true,
			hoverable: true,
		},
		legend: {
			labelBoxBorderColor: "ne",
			show: false,
		}
};
app.controller("buildController", function($scope, $http, $routeParams) {

	$scope.masterJobName = $routeParams.masterJobName
	$scope.showTestReport = "hidden";
	$scope.successfulTestClasses = null;
	$scope.unsuccessfulTestClasses = null; 
	$scope.threshold = 20; //GET FROM URL, SETTINGS
	$scope.currentTestClass = null
	$scope.currentTest = null

	var durationBuilds = new Array()
	var testBuilds = new Array()
	var testBuilds2 = new Array()
	var dataset; 
	$http.get(JATSERVER + 'job/' + $routeParams.childid + '/build').
	success(function(data, status, headers, config) {

		var buildColor = "";
		$scope.childid = $routeParams.childid;

		jQuery.each(data, function(i, val) {
			if(val.result=="SUCCESS"){
				buildColor = "#00FF00" //green
			} else {
				buildColor = "#FF0000" //red				
			}

			if(val.failCount != 0 || val.passCount != 0 || val.skipCount != 0) {

				var failedBuilds = {
						label: "Failed",
						color: '#ff0000',
						buildid: parseInt(val.id),
						teststatus: "failed",
						data: [
						       [val.buildNumber, val.failCount]
						       ]
				};

				var skippedBuilds = {
						label: "Skipped",
						color: "silver",
						buildid: parseInt(val.id),
						teststatus: "skipped",
						data: [
						       [val.buildNumber, val.skipCount]
						       ]
				};

				var passedBuilds = {
						label: "Passed",
						color: '#00FF00',
						buildid: parseInt(val.id),
						teststatus: "passed",
						data: [
						       [val.buildNumber,val.passCount]
						       ]
				};
				dataset = [failedBuilds, passedBuilds, skippedBuilds];
				testBuilds = testBuilds.concat(dataset);
			}
			durationBuilds.push(
					{
						data: [[val.buildNumber,
						        val.duration/1000]],
						        label: val.buildNumber,
						        color: buildColor,
						        buildid: parseInt(val.id),
						        duration: parseInt(val.duration/1000)
					} );


		});


		$('button').click(function(){
			var label = $(this).text();
			if (label.indexOf("Build Trend") > -1){
				$.plot($('#placeholder'), durationBuilds, optionsBar);
			} else if (label.indexOf("Test Trend") > -1){
				$.plot($('#placeholder'), testBuilds, optionsTests);
			}
		});

		if(testBuilds != null && testBuilds.length > 0) {
			$scope.testExists = true; 
			$.plot($('#placeholder'), testBuilds, optionsTests);
		} else {	
			$.plot($('#placeholder'), durationBuilds, optionsBar);
		}
	}).
	error(function(data, status, headers, config) {
		("Could not fetch jobs")
	});



	function showTooltip(x, y, contents) {
		$('<div id="tooltip">' + contents + '</div>').css( {
			position: 'absolute', display: 'none', top: y + 5, left: x + 5,
			border: '1px solid #fdd', padding: '2px', 'background-color': '#fee', opacity: 0.80
		}).appendTo("body").fadeIn(200);
	}

	$("#placeholder").bind("plothover", function (event, pos, item) {
		$("#tooltip").remove();
		if (item) {
			var x = item.datapoint[0].toFixed(2),y = item.datapoint[1].toFixed(2);
			if(item.series.duration != null){
				showTooltip(item.pageX, item.pageY, "Build duration: " + item.series.duration  + " s");
			} else {
				if(item.series.teststatus == "passed"){
					showTooltip(item.pageX, item.pageY, "Passed tests: " + (parseInt(item.datapoint[1]) - parseInt(item.datapoint[2])));
				}
				if(item.series.teststatus == "skipped") {
					showTooltip(item.pageX, item.pageY, "Skipped tests: " + (parseInt(item.datapoint[1]) - parseInt(item.datapoint[2])));				
				} 
				if(item.series.teststatus != "skipped" && item.series.teststatus != "passed")  {
					showTooltip(item.pageX, item.pageY, "Failed tests: " + item.datapoint[1]);
				}

			}
		}
	}); 


	function showBuildTestClass(buildid){

		$scope.loading = true	
		$http.get(JATSERVER + 'build/' + buildid + '/testclass/').
		success(function(data, status, headers, config) {
			$scope.buildid = buildid

			var container = []

			var containerFailed = []

			if(data.PASSED != null){
				$.each(data.PASSED, function(k, v) {
					var obj = {}


					obj.name = v.className
					obj.classNameId = v.id
					container.push(obj)
				});
			}
			if(data.FAILED != null){
				$.each(data.FAILED, function(k, v) {
					var obj = {}
					obj.name = v.className
					obj.classNameId = v.id
					containerFailed.push(obj)
				});
			}
			$scope.successfulTestClasses = container
			$scope.unsuccessfulTestClasses = containerFailed
		}).

		finally(function() {
			$scope.loading = false;
		});

		$http.get(JATSERVER + 'showbuild/' + buildid). //CONTAINS BUILD DATA
		success(function(data, status, headers, config) {

			$scope.showTestReport = "show"
				$scope.timestamp = new Date(data.timestamp);
			$scope.buildNumber = data.buildNumber;
			$scope.duration = (data.duration / 1000); 
			if(data.builtOnMachineName == null) {
				$scope.builtOnMachineName =  "Not Available" 
			} else {
				$scope.builtOnMachineName = data.builtOnMachineName; 
			} 
			$scope.failedTests = data.failCount; 
			$scope.successfulTests = data.passCount; 
			$scope.totalTests = data.failCount+data.passCount+data.skipCount; 

			if(data.gitSHA1 != null && data.gitSHA1.length > 0 ) {
				$scope.gitSHA1 = data.gitSHA1
				$scope.gitBranchName = data.gitBranchName 	
			} else {
				$scope.gitSHA1 = "Not available"
					$scope.gitBranchName = "Not available"	

			}

			if(data.buildUrl != null && data.buildUrl.length > 0) {
				$scope.buildUrl = data.buildUrl
			} else {
				$scope.buildUrl = "Not available"
			}


		});


	}
	$("#placeholder").bind("plotclick", function (event, pos, item) {		
		showBuildTestClass(item.series.buildid)
	});

	$scope.expandCallback = function (index) {

	};
	$scope.fetchTestPlot = function(testName) {	
		$scope.loadingTestPlot = true	
		var Example = (function() {
			"use strict";

			var elem,
			hideHandler,
			that = {};

			that.init = function(options) {
				elem = $(options.selector);
			};

			that.show = function(text) {
				clearTimeout(hideHandler);

				elem.find("span").html(text);
				elem.delay(200).fadeIn().delay(4000).fadeOut();
			};

			return that;
		}());

		var testBuilds = new Array()
		$http.get(JATSERVER + 'CHILD/' + $scope.childid + '/compareTests/' + testName).
		success(function(data, status, headers, config) {
			var buildColor = "";
			jQuery.each(data, function(i, val) {
				if(val.test[0].status =="PASSED" || val.test[0].status =="FIXED"){
					buildColor = "#00FF00" 
				}
				if(val.test[0].status =="SKIPPED"){
					buildColor = "silver" 
				}				
				if(val.test[0].status != "PASSED" && val.test[0].status != "SKIPPED" && val.test[0].status != "FIXED") {
					buildColor = "#FF0000" 				
				}

				var test = {
						color: buildColor,
						buildid: parseInt(val.buildNumber),
						errorStackTrace: val.test[0].errorStackTrace,
						duration: val.test[0].duration,
						status: val.test[0].status,

						data: [
						       [val.buildNumber,val.test[0].duration]
						       ]
				}

				var durationTmp;
				if(val.test[0].duration == 0) {
					test.points = {
							show:true,
							radius: 3
					}
				}


				testBuilds = testBuilds.concat(test);
			});
			$scope.testBuilds = testBuilds;
			$.plot($('#placeholder-'+ testName), testBuilds, optionsTestBar);

			$("#placeholder-"+ testName).bind("plothover", function (event, pos, item) {
				$("#tooltip").remove();
				if(item) {
					showTooltip(item.pageX, item.pageY, "Duration: " + item.series.duration + ' ms');
				}
			});

			$('#placeholder-'+ testName).bind("plotclick", function (event, pos, item) {					
				var message = 
					'<div class="row">  ' +
					'<div class="col-md-12"> ' +
					'<h4>STATUS</h4> ' +
					'<div>' +item.series.status + '</div> ' +
					'</div> </div> </div>' + 
					'<div class="row">  ' +
					'<div class="col-md-12"> ' +
					'<h4>DURATION</h4> ' +
					'<div>' +item.series.duration + ' ms </div> ' +
					'</div> </div> </div>'; 
				if(item.series.errorStackTrace != null) {
					message += 
						'<div class="row">  ' +
						'<div class="col-md-12"> ' +
						'<h4>STACKTRACE</h4> ' +
						'</div> </div>' +
						'<div class="row">  ' +
						'<div class="col-md-12" style="word-break: break-word;"> ' +item.series.errorStackTrace +
						'</div> </div>'; 
				}
				bootbox.alert({
					title: "Test Overview Build #" + item.series.buildid,
					message: message
				});
			}); 
		}).
		error(function(data, status, headers, config) {
			$scope.testData = "";
		}).

		finally(function() {
			$scope.loadingTestPlot = false;
		});
	};

	$scope.fetchTestData = function(buildid, testclass, testclasses, statusName) {
		$http.get(JATSERVER + 'build/' + buildid + '/testclass/' + testclass + '/test').
		success(function(data, status, headers, config) {
			$scope.testData = "";
			$scope.passedTests = data.PASSED;
			var container = []
			var containerFailed = []
			var containerSkipped = []
			var containerSuccessInFailesTestClass = []

			if(statusName == 1){


				$.each(testclasses, function(k, v) {

					if(testclass == v.classNameId){
						v.successfulTestCases = data.PASSED
					}
					container.push(v)
				});
				$scope.successfulTestClasses = container

			}

			if(statusName == 0){

				if(data.FAILED != null){
					$.each(testclasses, function(k, v) {
						if(testclass == v.classNameId){
							v.unsuccessfulTestCases = data.FAILED
						}
						containerFailed.push(v)
					});
					$scope.unsuccessfulTestCases = containerFailed
				}

				if(data.SKIPPED != null){
					$.each(testclasses, function(k, v) {
						if(testclass == v.classNameId){
							v.skippedTestCases = data.SKIPPED
						}
						containerSkipped.push(v)
					});
					$scope.skippedTestCases = containerSkipped
				}

				if(data.PASSED != null){
					$.each(testclasses, function(k, v) {
						if(testclass == v.classNameId){
							v.successfulTestCasesFailedClass = data.PASSED
						}
						containerSuccessInFailesTestClass.push(v)
					});
					$scope.unsucessfulTestclass = containerSuccessInFailesTestClass
				}
			}
		}).
		error(function(data, status, headers, config) {
			$scope.testData = "";
		});
	};

});
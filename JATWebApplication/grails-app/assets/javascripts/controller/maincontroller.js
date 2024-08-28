app.controller("mainController", function($scope, $http, $routeParams) { 
	$scope.loadJobs = function() {
		$scope.loading = true
		$scope.currentView = 'ALL'
			var container = []

		$http.get(JATSERVER + 'getjobwithbuilds').
		success(function(data, status, headers, config) {

			$.each(data, function(k, v) {
				container.push(v.jobName)
			});

			$scope.filteredJobs = container
			$scope.jobs = data;
			loadJobsPie(container)
		}).
		error(function(data, status, headers, config) {
			console.log("Could not fetch jobs")
		}).
		finally(function() {
			$scope.loading = false;
		});
	}
	
	$scope.loadJobs()	
	function loadJobsPie(jobList){
		var options = {
				series: {
					pie: {
						show: true,
						label: false
					}
				},
				legend: {
					show: false
				},
				grid: {
					hoverable: true
				}
		}
		function showTooltip(x, y, contents) {
			$('<div id="tooltip">' + contents + '</div>').css( {
				position: 'absolute', display: 'none', top: y + 15, left: x + 15,
				border: '1px solid #fdd', padding: '2px', 'background-color': '#fee', opacity: 0.80
			}).appendTo("body").fadeIn(200);
		}


		$http.get(JATSERVER + 'getjobwithbuilds').
		success(function(data, status, headers, config) {
			if(data.length > 0) { 
				data.forEach(function(obj) {
					if(jobList.indexOf(obj.jobName) > -1) {
						var pieChartDataList = []
						var pieChartDataJson = {}

						pieChartDataJson.label = "PASSED";
						pieChartDataJson.color = "#16df2d";
						pieChartDataJson.data = obj.builds.RECENT.passCount;
						pieChartDataList.push(pieChartDataJson)
						pieChartDataJson = {}
						pieChartDataJson.label = "FAILED";
						pieChartDataJson.color = "#ED1B24";
						pieChartDataJson.data = obj.builds.RECENT.failCount;
						pieChartDataList.push(pieChartDataJson)
						var pieChartDataJson = {}

						pieChartDataJson.label = "SKIPPED";
						pieChartDataJson.color = "silver";
						pieChartDataJson.data = obj.builds.RECENT.skipCount;
						pieChartDataList.push(pieChartDataJson)
						var pieChartDataJson = {}
						$.plot($("#" + obj.jobName.split('.').join('_')), pieChartDataList, options);


						$("#" + obj.jobName.split('.').join('_')).bind("plothover", function (event, pos, item) {
							$("#tooltip").remove();
							if (item) {
								if(item.series.label == "PASSED")  {
									showTooltip(pos.pageX, pos.pageY, "Passed tests: " + Math.round(item.datapoint[0],2) + " %");
								} 
								if(item.series.label == "SKIPPED")  {
									showTooltip(pos.pageX, pos.pageY, "Skipped tests: " + Math.round(item.datapoint[0],2) + " %");
								} 
								if(item.series.label == "FAILED")  {
									showTooltip(pos.pageX, pos.pageY, "Failed tests: " + Math.round(item.datapoint[0],2) + " %");
								} 
							}
						}); 
					}
				});
			}
		}).
		error(function(data, status, headers, config) {
			console.log("Could not fetch jobs")
		});
	}


	function getViews() {

		$http.get(JATSERVER + 'jobview').
		success(function(data, status, headers, config) {

			if(data.length > 0) { 
				$scope.jobViews = data
			}
		}).
		error(function(data, status, headers, config) {
			console.log("Could not fetch jobs")
		});
	}

	getViews()

	$scope.selectView = function(view,viewId) {
		var tmp = []
		var container = []
		$scope.filteredJobs = []
		$scope.currentView = view
		
		$http.get(JATSERVER + "getjobviews/" + viewId).
		success(function(data, status, headers, config) {

			if(data.length > 0) {

				$.each(data, function(k, v) {
					container.push(v.jobName)
				});

				$scope.filteredJobs = container

			}
		}).
		error(function(data, status, headers, config) {
			console.log("Could not fetch jobs")
		}). 
		finally(function(){
			loadJobsPie(container)
		}); 
	}

});
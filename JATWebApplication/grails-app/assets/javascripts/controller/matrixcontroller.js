app.controller("matrixjobController", function($scope, $http, $routeParams) { 

	$scope.loadJobs = function() {
		$http.get(JATSERVER + 'getrecentbuildinchildjobs/' + $routeParams.matrixid).
		success(function(data, status, headers, config) {
			$scope.childjobs = data;
			$scope.matrixjobname = $routeParams.matrixid;
		}).
		error(function(data, status, headers, config) {
			console.log("Could not fetch jobs")
		}).
		finally(function(){
			$scope.loadJobsPie();
		});

	}


	$scope.loadJobs();
	$scope.Math = window.Math;
	$scope.loadJobsPie = function() {

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
		$http.get(JATSERVER + 'getrecentbuildinchildjobs/' + $routeParams.matrixid).
		success(function(data, status, headers, config) {
			if(data.length > 0) { 
				data.forEach(function(obj) {
					var pieChartDataList = []
					var pieChartDataJson = {}

					pieChartDataJson.label = "PASSED";
					pieChartDataJson.color = "#16df2d";
					pieChartDataJson.data = obj.build[0].passCount;
					pieChartDataList.push(pieChartDataJson)
					pieChartDataJson = {}
					pieChartDataJson.label = "FAILED";
					pieChartDataJson.color = "#ED1B24";
					pieChartDataJson.data = obj.build[0].failCount;
					pieChartDataList.push(pieChartDataJson)
					var pieChartDataJson = {}

					pieChartDataJson.label = "SKIPPED";
					pieChartDataJson.color = "silver";
					pieChartDataJson.data = obj.build[0].skipCount;
					pieChartDataList.push(pieChartDataJson)
					var pieChartDataJson = {}

					$.plot($("#" + (((obj.jobName).split('.').join('_')).split(',').join('_')).split(' ').join('_')), pieChartDataList, options);
					$("#" + (((obj.jobName).split('.').join('_')).split(',').join('_')).split(' ').join('_')).bind("plothover", function (event, pos, item) {
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
				});
			}
		}).
		error(function(data, status, headers, config) {
			console.log("Could not fetch jobs")
		});


	}

});
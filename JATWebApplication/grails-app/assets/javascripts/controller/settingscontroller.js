app.controller("settingsController", function($scope, $http) {

	function getJobs() {

		$http.get(JATSERVER + 'getjobwithbuilds').
		success(function(data, status, headers, config) {

			if(data.length > 0) { 
				$scope.jobs = data
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
	getJobs()

	$scope.removeJob = function(jobName,index) {

		bootbox.confirm("Are you sure you want to delete " + jobName + "?", function(result) {

			if(result) {
				$http.delete(JATSERVER +'job/' + jobName).
				success(function(data, status, headers, config) {
					$scope.jobs.splice(index, 1)

				}).
				error(function(data, status, headers, config) {
					console.log("Could not remove job")
				});
			}
		});



	}

	$scope.removeView = function(view,index) {

		bootbox.confirm("Are you sure you want to delete " + view.viewName + "?", function(result) {
			if(result) {
				$http.delete(JATSERVER +'jobview/' + view.id).
				success(function(data, status, headers, config) {
					$scope.jobViews.splice(index, 1)
					$scope.currentView = null
					getViews()

				}).
				error(function(data, status, headers, config) {
					console.log("Could not remove view")
				});
			}
		});
	}

	$scope.createView = function(viewName) {

		$http({
			method: "post",
			url: JATSERVER + "jobview",
			data: {
				viewName: viewName
			}
		}).
		success(function(data, status, headers, config) {
			getViews()
		}).
		error(function(data, status, headers, config) {
			bootbox.alert("View already exist");
		});


	}

	$scope.selectView = function(view,viewId) {
		var container = []

		$scope.currentViewJobs = []
		$scope.currentView = view
		getJobs()

		$http.get(JATSERVER + "getjobviews/" + viewId).
		success(function(data, status, headers, config) {


			if(data.length > 0) { 

				$.each(data, function(k, v) {
					container.push(v.jobName)
				});
				$scope.currentViewJobs = container
			}


			getViews()
		}).
		error(function(data, status, headers, config) {
			console.log("Could not fetch jobs")
		});
	}

	$scope.bindJob = function(viewId, jobName) {

		console.log(viewId)
		console.log(jobName)
		$http({
			method: "post",
			url: JATSERVER + "addjob",
			data: {
				jobViewId: viewId,
				jobName: jobName
			}
		}).
		success(function(data, status, headers, config) {
		});

	}
});
<div class="prods-cnt" data-ng-controller="settingsController">
<div style="border: 1px #ccc dotted; border-radius: 5px; padding: 30px">
	<h2>CREATE CUSTOM VIEWS</h2>

	<br>
	<br>
	<div class="row">
		<div class="col-md-10">
			<input class="form-control" placeholder="View Name" data-ng-model="viewName"></input>
		</div>
		<div class="col-md-2">
			<button type="button" class="btn btn-primary" data-ng-click="createView(viewName)">Create View</button>
		</div>
	</div>
	<br/>
	<ul class="nav nav-pills">
  <li data-ng-class="{'active':view.viewName == currentView.viewName}" data-ng-repeat="view in jobViews"><a style="cursor: pointer;" data-ng-click="selectView(view, view.id)">{{view.viewName}}</a></li>
</ul>
<br/>
<input type="text" data-ng-model="jobFilter" placeholder="Search job.." class="form-control" style="width:350px">
	<br>
	<br>
	<div class="row">
		<div class="col-md-12">
			<table class="table">
				<thead>
					<tr>
						<td style="font-size: 14px;"><strong>Jenkins Job</strong></td>
						<td style="font-size: 14px;"><strong>Job Type</strong></td>
						<td style="font-size: 14px;"><strong>Current Status</strong></td>
						<td style="font-size: 14px;"><strong>Select</strong></td>				
					</tr>
				</thead>
				<tr data-ng-repeat="job in jobs | filter: jobFilter">

					<td>{{job.jobName}}</td>
					<td>{{job.jobType}}</td>
					<td>{{job.builds.RECENT.result}}</td>
					<td><input data-ng-if="currentView != null" data-ng-bind="currentViewJobs.indexOf(job.jobName) > -1" 
							   type="checkbox" 
							   data-ng-click="bindJob(currentView.id, job.jobName)" 
							   data-ng-checked="currentViewJobs.indexOf(job.jobName) > -1"> <span data-ng-if="currentView == null"></span></td>
				</tr>
			</table>
		</div>
	</div>

		<div class="row">
		<div class="col-md-10">
		</div>
		<div class="col-md-2">
	<button data-ng-if="currentView != null" type="button" class="btn btn-primary" data-ng-click="removeView(currentView,$index)">Delete view</button>
		</div>
	</div>
	</div>

	<div style="border: 1px #ccc dotted; border-radius: 5px; padding: 30px;margin-top:20px">
	<h2>DELETE JOBS</h2>
	<div class="row">
		<div class="col-md-12">
			<table class="table">
				<thead>
					<tr>
						<td style="font-size: 14px;"><strong>Jenkins Job</strong></td>
						<td style="font-size: 14px;"><strong>Job Type</strong></td>
						<td style="font-size: 14px;"><strong>Current Status</strong></td>
						<td style="font-size: 14px;"><strong>Remove</strong></td>				
					</tr>
				</thead>
				<tr data-ng-repeat="job in jobs">
					<td>{{job.jobName}}&nbsp;</td>
					<td>{{job.jobType}}&nbsp;</td>
					<td>{{job.builds.RECENT.result}}&nbsp;</td>
					<td><button class="glyphicon glyphicon-remove" data-ng-click="removeJob(job.jobName, $index)"></button>&nbsp;</td>
				</tr>
			</table>
		</div>
	</div>
</div>
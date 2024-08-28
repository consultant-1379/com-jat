<div id="wrapper" data-ng-controller="mainController">
	<div class="prods-cnt">
		<div class="clear"></div>

		<h3 style="padding: 0; margin: 0; padding-bottom: 20px; font-family: trebuchet MS">Available views</h3>
		<ul class="nav nav-pills" style=" width: 1008px; border: 1px #ccc dotted; border-radius: 5px; padding: 20px" >
		<li data-ng-class="{'active':'ALL' == currentView}"><a style="cursor: pointer;" data-ng-click="loadJobs()">ALL</a></li>
	  	<li data-ng-class="{'active':view.viewName == currentView.viewName}" data-ng-repeat="view in jobViews"><a style="cursor: pointer;" data-ng-click="selectView(view, view.id)">{{view.viewName}}</a></li>
		</ul>
		<div class="form-group">
			<h3 style="padding: 0; margin-left: 0px; padding-bottom: 20px; font-family: trebuchet MS">Jenkins jobs</h3	>
		</div>
		<div class="prod-cnt prod-box-list shadow"
			data-ng-repeat="job in jobs" 
			data-ng-if="filteredJobs.indexOf(job.jobName) > -1"
			style="background-color: white; padding: 0; margin-right: 8px;"
			data-ng-style="
			job.builds.RECENT.result === 'SUCCESS' && {'border-top': '30px solid #16df2d'}
			|| job.builds.RECENT.result === 'FAILURE' && {'border-top': '30px solid #ED1B24'}
			|| job.builds.RECENT.result === 'ABORTED' && {'border-top': '30px solid #A8A8A8'}
			|| job.builds.RECENT.result === 'UNSTABLE' && {'border-top': '30px solid #ffed39'}">
			<div data-ng-if="job.jobType == 'SINGLETON'"
					style="padding-left: 10px;margin-top:-22px;">
	
					<a href="#/CHILD/{{job.jobName}}"
						title="{{job.jobName}}" style="font-size: 16px; color: #000000; font-weight: bold; font-family: trebuchet MS">{{job.jobName | limitTo:40}}{{job.jobName.length > 40 ? '...' : ''}} </a>
					<span class="pull-right" style="margin-top: 8px; padding-right: 10px; font-size: 8px; color: #000000; font-weight: bold; font-family: trebuchet MS">{{job.builds.RECENT.result}}</span>				
				</div>

				<div data-ng-if="job.jobType == 'MATRIX'" style="padding-left: 10px;margin-top:-22px;">
					<a  href="#/{{job.jobType}}/{{job.jobName}}" title="{{job.jobName}}"
						style="font-size: 16px; color: #000000; font-weight: bold; font-family: trebuchet MS">{{job.jobName | limitTo: 40}}{{job.jobName.length > 40 ? '...' : ''}} </a>
					<span class="pull-right" style="margin-top: 8px; padding-right: 10px; font-size: 8px; color: #000000; font-weight: bold; font-family: trebuchet MS">{{job.builds.RECENT.result}}</span>
				</div>
			<div id="{{(job.jobName).split('.').join('_')}}" class="plot"
				style="float: right; margin-right: 20px; width: 125px; height: 125px; margin-top: 45px;">
			</div>
			<br>
			<table style="width:320px">
					<tr>
					  <th style="font-size: 14px;border-right:1px #b6bfb7 solid; font-family: arial">STARTED </th>
					  <td style="padding-left:10px">{{job.builds.RECENT.timestamp | date:'MM/dd/yyyy HH:mm:ss '}}</td>
					</tr>
					<tr>
					  <th style="font-size: 14px;border-right:1px #b6bfb7 solid; font-family: arial">DURATION</th>
					  <td style="padding-left:10px">{{job.builds.RECENT.duration / 1000}}  s</td>
					</tr>
					<tr>
					  <th style="font-size: 14px;border-right:1px #b6bfb7 solid; font-family: arial">EXECUTED TESTS </th>
					  <td style="padding-left:10px">{{job.builds.RECENT.passCount + job.builds.RECENT.failCount }}
					  </td>
					</tr>
					<tr>
					  <th style="font-size: 14px;border-right:1px #b6bfb7 solid; font-family: arial">FAILED TESTS </th>
					  <td style="padding-left:10px">{{job.builds.RECENT.failCount }}
					  </td>
					</tr>					
					<tr>
						<th style="font-size: 14px;border-right:1px #b6bfb7 solid; font-family: arial">LAST FAILED</th>
					 	<td style="padding-left:10px">{{job.builds.FAILURE.timestamp | date:'MM/dd/yyyy HH:mm:ss '}}
					 		<span data-ng-if="job.builds.FAILURE == null">No failed build</span>
					 	</td>	
					</tr>
					
			</table></div>
		<div data-ng-show="loading" style="margin-left:500px">
		<img src="assets/ajax-loader.gif" /></div>
	</div>

</div>
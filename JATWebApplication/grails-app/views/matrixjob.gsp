<div id="wrapper" data-ng-controller="matrixjobController">
	<div class="prods-cnt">
		<h2 style="margin-left: 15px; font-size: 16px;">
			<a href="/#/">All jobs</a> > {{ matrixjobname }}
		</h2>

		<div class="prod-cnt prod-box shadow"
			style="background-color: white; padding: 0; margin-right: 8px;"
			data-ng-repeat="job in childjobs"
			data-ng-style="job.build[0].result === 'SUCCESS' && {'border-top': '30px solid #16df2d'}
					|| job.build[0].result === 'FAILURE' && {'border-top': '30px solid #ED1B24'}
					|| job.build[0].result === 'ABORTED' && {'border-top': '30px solid #A8A8A8'}
					|| job.build[0].result === 'UNSTABLE' && {'border-top': '30px solid #ffed39'}">


			<div style="margin-top: -22px; margin-left: 5px;">
				<a href="#/{{job.jobType}}/{{job.jobName}}/{{ matrixjobname }}"
					title="{{job.jobName}}"
					style="font-size: 14px; color: #000000; font-weight: bold; font-family: trebuchet MS">{{job.jobName.replace(matrixjobname,'')
					| limitTo: 25}}{{job.jobName.length > 25 ? '...' : ''}}</a>
					<span class="pull-right" style="margin-top: 8px; padding-right: 10px; font-size: 8px; color: #000000; font-weight: bold; font-family: trebuchet MS">{{job.build[0].result}}</span>					
			</div>
			<div id="{{(((job.jobName).split('.').join('_')).split(',').join('_')).split(' ').join('_')}}"  
			style="margin-left: 185px; width: 100px; height: 100px; margin-top: 10px;"></div>
			<table style="margin-left: 5px; margin-top: -70px; width:180px;">
					<tr>
					  <th style="font-size: 11px;border-right:1px #b6bfb7 solid; font-family: arial">DURATION</th>
					  <td style="padding-left:10px">{{ Math.round(job.build[0].duration/1000)}} s</td>
					</tr>		
					<tr>
					  <th style="font-size: 11px;border-right:1px #b6bfb7 solid; font-family: arial">EXECUTED TESTS</th>
					  <td style="padding-left:10px">{{job.build[0].passCount + job.build[0].failCount}}</td>
					</tr>
					<tr>
					  <th style="font-size: 11px;border-right:1px #b6bfb7 solid; font-family: arial">FAILED TESTS</th>
					  <td style="padding-left:10px">{{job.build[0].failCount}}</td>
					</tr>							
			</table>
		</div>
	</div>

</div>

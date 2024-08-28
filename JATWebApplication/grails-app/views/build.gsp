<div id="wrapper" data-ng-controller="buildController">
	<div class="prods-cnt">

		<h2 style="font-size: 16px;margin-left: 15px;">
			<a href="/#/">All jobs</a> > <span data-ng-if="masterJobName.length > 0"><a href="#/MATRIX/{{masterJobName}}"">{{ masterJobName }}</a>  ></span> {{ childid }}
		</h2>


		<div id="placeholder" style="width: 1050px; height: 250px;"></div>
		<div data-ng-if="testExists">
		<button type="button" class="btn btn-primary">Test Trend</button>
		<button type="button" class="btn btn-primary">Build Trend</button>
		</div>
		<div data-ng-if="buildNumber != null">
			<h4>Overview of Build #{{buildNumber}}</h4>
			<table class="table">
				<thead>
					<tr>
						<td>Time Started</td>
						<td>Duration (s)</td>
						<td>Machine</td>
						<td>Revision SHA1</td>
						<td>Branch</td>
						<td>Jenkins Url</td>
					</tr>
				</thead>
				<tr>
					<td>{{timestamp | date:'MM/dd/yyyy HH:mm:ss '}}</td>
					<td>{{duration}}</td>
					<td>{{builtOnMachineName}}</td>
					<td>{{gitSHA1}}</td>
					<td>{{gitBranchName}}</td>
					<td><a target="_blank" href="{{buildUrl}}">Open Jenkins</a></td>
				</tr>
			</table>
		</div>
	</div>


	<div data-ng-include="'testresults.htm'"></div>

</div>

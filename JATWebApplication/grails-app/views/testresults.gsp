<div class="prods-cnt" data-ng-controller="buildController"
	style="visibility: {{showTestReport}}">
	<div data-ng-show="loading" style="margin-left:500px">
	<img src="assets/ajax-loader.gif" /></div>
		<div class="container-fluid" data-ng-if="buildNumber != null">
		<div style="border: 1px #ccc dotted; border-radius: 5px; padding: 30px" 
			 data-ng-if="unsuccessfulTestClasses.length > 0">
			 <div class="row"><div class="col-md-10"><h2 style="font-size:28px;margin:0;padding:0;width:350px;float:left;margin-bottom:10px">FAILED TEST CLASSES</h2> <span class="glyphicon glyphicon-remove" style="font-size:18px;color:#FF0000;margin-top:4px"></span></div></div>
			
			<v-accordion class="vAccordion--default">
			  <v-pane data-ng-repeat="unsucessfulTestclass in unsuccessfulTestClasses">
			    <v-pane-header style="font-size: 18px"  data-ng-click="fetchTestData(buildid,unsucessfulTestclass.classNameId,unsuccessfulTestClasses,0)">
			      {{unsucessfulTestclass.name}}
			    </v-pane-header>
			    <v-pane-content>
			   
			    <h3 data-ng-if="unsucessfulTestclass.unsuccessfulTestCases != null">UNSUCCESSFUL TEST CASES</h3>
			      <v-accordion>
			        <v-pane data-ng-repeat="unsuccessfulTest in unsucessfulTestclass.unsuccessfulTestCases">
			          <v-pane-header style="font-size: 14px"  data-ng-click="fetchTestPlot(unsuccessfulTest.testName)">
			           {{unsuccessfulTest.testName}}
			          </v-pane-header>
			          <v-pane-content>
									<br>
									<div data-ng-show="loadingTestPlot" style="margin-left:450px"> <img src="assets/ajax-loader.gif" /></div>
									<div id="placeholder-{{unsuccessfulTest.testName}}" style="width: 900px; height: 250px;"></div>
					</v-pane-content>
			        </v-pane>
			      </v-accordion>
			      
			      <h3 data-ng-if="unsucessfulTestclass.skippedTestCases != null">SKIPPED TEST CASES</h3>
			      <v-accordion>
			        <v-pane data-ng-repeat="skippedTestCase in unsucessfulTestclass.skippedTestCases">
			          <v-pane-header style="font-size: 14px"  data-ng-click="fetchTestPlot(skippedTestCase.testName)">
			           {{skippedTestCase.testName}}
			          </v-pane-header>
			          <v-pane-content>
									<br>
									<div data-ng-show="loadingTestPlot" style="margin-left:450px"> <img src="assets/ajax-loader.gif" /></div>									
									<div id="placeholder-{{skippedTestCase.testName}}" style="width: 900px; height: 250px;"></div>
					</v-pane-content>
			        </v-pane>
			      </v-accordion>
			      
			      <h3 data-ng-if="unsucessfulTestclass.successfulTestCasesFailedClass != null">SUCCESSFUL TEST CASES</h3>
			      <v-accordion>
			        <v-pane data-ng-repeat="successfulTest in unsucessfulTestclass.successfulTestCasesFailedClass">
			          <v-pane-header style="font-size: 14px" data-ng-click="fetchTestPlot(successfulTest.testName)">
			           {{successfulTest.testName}}
			          </v-pane-header>
			          <v-pane-content>
									<br>
									<div data-ng-show="loadingTestPlot" style="margin-left:450px"> <img src="assets/ajax-loader.gif" /></div>
									<div id="placeholder-{{successfulTest.testName}}" style="width: 900px; height: 250px;"></div>
					</v-pane-content>
			        </v-pane>
			      </v-accordion>
			   
			    </v-pane-content>
			  </v-pane>
			</v-accordion>
		</div>
		<div style="border: 1px #ccc dotted; border-radius: 5px; padding: 30px; margin-top: 15px" 
			 data-ng-if="successfulTestClasses.length > 0">
			 <div class="row"><div class="col-md-10"><h2 style="font-size:28px;margin:0;padding:0;width:350px;float:left;margin-bottom:10px">PASSED TEST CLASSES</h2> <span class="glyphicon glyphicon-ok" style="font-size:18px;color:#00FF00;margin-top:4px"></span></div></div>

			<v-accordion class="vAccordion--default">
			  <v-pane data-ng-repeat="successfultestclass in successfulTestClasses">
			    <v-pane-header style="font-size: 18px"  data-ng-click="fetchTestData(buildid,successfultestclass.classNameId,successfulTestClasses,1)"
			    			   id="pane{{$index}}-header" aria-controls="pane{{$index}}-content">
			      {{successfultestclass.name}}
			    </v-pane-header>
			    <v-pane-content  id="pane{{$index}}-content" aria-labelledby="pane{{$index}}-header">
			      <v-accordion>
			        <v-pane 
			        		data-ng-repeat="successfulTest in successfultestclass.successfulTestCases">
			          <v-pane-header style="font-size: 14px" data-ng-click="fetchTestPlot(successfulTest.testName)">
			           {{successfulTest.testName}}
			          </v-pane-header>		         
			          <v-pane-content>
									<br>
									<div data-ng-show="loadingTestPlot" style="margin-left:450px"> <img src="assets/ajax-loader.gif" /></div>
									<div id="placeholder-{{successfulTest.testName}}" style="width: 900px; height: 250px;"></div>
					</v-pane-content>
			        </v-pane>
			      </v-accordion>
			    </v-pane-content>		    
			  </v-pane>
			</v-accordion>
		</div>
	</div>
</div>



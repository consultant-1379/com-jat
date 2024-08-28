<!DOCTYPE html>
<html data-ng-app="JAT">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JAT Web Application</title>
<asset:stylesheet src="application.css" />

<script src="assets/angular.js"></script>
<script src="assets/angular-route.js"></script>
<script src="assets/v-accordion.js"></script>
<script src="assets/angular-animate.js"></script>
<script src="assets/angularinit.js"></script>
<script src="assets/jquery.js"></script>
<script src="assets/jquerycookie.js"></script>
<script src="assets/bootstrap.js"></script>
<script src="assets/controller/maincontroller.js"></script>
<script src="assets/controller/matrixcontroller.js"></script>
<script src="assets/controller/buildcontroller.js"></script>
<script src="assets/controller/statisticscontroller.js"></script>
<script src="assets/controller/settingscontroller.js"></script>
<script src="assets/controller/userguidecontroller.js"></script>
<script src="assets/jqueryflot/excanvas.js"></script>
<script src="assets/jqueryflot/jqueryflot.js"></script>
<script src="assets/jqueryflot/jqueryflotcategories.js"></script>
<script src="assets/jqueryflot/jqueryflotorderbars.js"></script>
<script src="assets/jqueryflot/jqueryflotpie.js"></script>
<script src="assets/jqueryflot/jqueryflottime.js"></script>
<script src="assets/jqueryflot/jqueryflotsymbol.js"></script>
<script src="assets/jqueryflot/jqueryflotselectionmin.js"></script>
<script src="assets/jqueryflot/jqueryflotaxislabels.js"></script>
<script src="assets/jqueryflot/jqueryflotcanvas.js"></script>
<script src="assets/jqueryflot/jquery.flot.stack.js"></script>
<script src="assets/bootbox.min.js"></script>
<script src="assets/konami.js"></script>
<script src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.10.0.js"></script>
<script type="text/javascript">

var easter_egg = new Konami()
easter_egg.load('https://youtu.be/0MpbRj15hI0?t=1m57s')

    // IE9 fix
    if(!window.console) {
        var console = {
            log : function(){},
            warn : function(){},
            error : function(){},
            time : function(){},
            timeEnd : function(){}
        }
    }
</script>
</head>
<body>


	<nav class="navbar navbar-inverse">
		<div class="container-fluid">

			<div>
				<ul class="nav navbar-nav menu">
					
					<li><a href="#/" style="font-family: ericssonfont;">HOME</a></li>
					<li><a href="#/settings" style="font-family: ericssonfont;">SETTINGS</a></li> 
					<li><a href="#/userguide" style="font-family: ericssonfont;">ABOUT</a></li>
					<!-- <li><a href="#/statistics" style="font-family: ericssonfont;">STATISTICS</a></li> -->

					<!--<li><a href="#" style="font-family: ericssonfont;">SIGN
							OUT</a></li>-->

				</ul>
				<a target="_blank" href="http://www.ericsson.com"><img
					src="assets/ericssonlogo.jpg" class="pull-right brand"
					style="height: 100px; width: 120px; margin-top: 5px; border-radius: 7px;" /></a>
			</div>
		</div>
	</nav>

	<div class="container-fluid">


		<div id="main">
			<div data-ng-view></div>
		</div>
	</div>
</body>
</html>

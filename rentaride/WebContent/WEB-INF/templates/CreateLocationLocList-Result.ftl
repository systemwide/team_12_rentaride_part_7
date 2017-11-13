<!doctype html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title></title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="apple-touch-icon" href="apple-touch-icon.png">

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <style>
            body {
                padding-top: 50px;
                padding-bottom: 20px;
            }
        </style>
        <link rel="stylesheet" href="css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="css/main.css">

        <script src="js/vendor/modernizr-2.8.3-respond-1.4.2.min.js"></script>
    </head>
<body>


    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Rent-A-Ride</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <form class="navbar-form navbar-right" role="form">
            <span class="welcome_message">Welcome, Team Twelve Admin</span>
            <a href="user_profile.html">
              <span class="glyphicon glyphicon-user"></span>
            </a>

            <a href="settings.html">
               <span class="glyphicon glyphicon-cog"></span>
            </a>

        
            <button type="submit" class="btn btn-success" id="logOut">Log Out</button>
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>

   <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li class="active"><a href="#">Overview <span class="sr-only">(current)</span></a></li>
            <li><a href="admin_location.html">Manage Locations</a></li>
            <li><a href="admin_vehicle.html">Manage Vehicles</a></li>
            <li><a href="admin_user_mgmt.html">Manage Users</a></li>
            <li><a href="admin_reservations.html">Manage Reservations</a></li>
          </ul>
          <ul class="nav nav-sidebar">
            <li><a href="admin_create_type.html">Manage Vehicle Types</a></li>
            <li><a href="admin_fees.html">Manage Fees</a></li>
            <li><a href="">One more nav</a></li>
            <li><a href="">Another nav item</a></li>
            <li><a href="">More navigation</a></li>
          </ul>
          <ul class="nav nav-sidebar">
            <li><a href="">Nav item again</a></li>
            <li><a href="">One more nav</a></li>
            <li><a href="">Another nav item</a></li>
          </ul>
        </div> <!--end sidenav -->

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h1 class="page-header">Manage Rental Locations</h1>

          <div class="row placeholders">
          <div class="col-sm-6">
            <form id="createLocation" method="post" action="AddRentalLoc">
            <fieldset>
            <legend>Create New Location</legend>
            <p><label class="field" for="locName">Location Name:</label><input class="textbox-200" type="text" placeholder="Name" id="locName"></p>
            <p><label class="field" for="locAddress">Location Address:</label><input class="textbox-200" type="text" placeholder="Address" id="locAddress"></p>
           <!-- <p><label class="field" for="locCoord">Coordinates:</label><input class="textbox-200" type="text" placeholder="Coordinates" id="locCoord"></p>-->
            <p><label class="field" for="locCapacity">Location Capacity:</label><input class="textbox-200" type="text" placeholder="Capacity" id="locCapacity"></p>
            <!--<p><label class="field" for="addVehicle">Add Vehicle (by VIN):</label><input class="textbox-200" type="text" placeholder="New Vehicle VIN" id="addVehicle"></p>-->
            <!--<p><label class="field" for="removeVehicle">Remove Vehicle (by VIN):</label><input class="textbox-200" type="text" placeholder="Removed Vehicle VIN" id="remVehicle"></p>-->
            </fieldset>
            <p><input type="submit" class="submit" id="vehicleCreator" value="Submit" title="Submit this form and create this location"></p>

            </form>
          </div>
          <div class="col-sm-6">
          <img src="img/prius.jpeg" class="img-responsive" alt="prius">
          </div>
          </div><!-- end row-->

          <h2 class="sub-header">Your Rental Locations</h2>
	  <form method="POST" action="ManageRentalLoc">
	    <table id="rentLoc">
	      <thead>
		<tr><th>Location Name</th><th>Address</th><th>Capacity</th><th>Actions</th></tr>
	      </thead>
	      <tbody>
		<#list locations as location>
		  <tr><td><input type="text" value="${location[1]}" id="editLocName"></td><td><input type="text" value="${location[2]}" id="editLocAddress"></td><td><input type="text" value="${location[3]}" id="editLocCap"></td><td><button name="updateLoc" value="${location[0]}" type="submit">Update</button><button name="deleteLoc" value="${location[0]}" type="submit">Delete</button></tr>
		</#list>
	      </tbody>
	    </table>
	  </form>
      <hr>

      <footer>
        <p>&copy; Company 2015</p>
      </footer>
    </div> <!-- /container -->        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.11.2.min.js"><\/script>')</script>

        <script src="js/vendor/bootstrap.min.js"></script>

        <script src="js/main.js"></script>

        <!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
<!--        <script>
            (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=
            function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;
            e=o.createElement(i);r=o.getElementsByTagName(i)[0];
            e.src='//www.google-analytics.com/analytics.js';
            r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
            ga('create','UA-XXXXX-X','auto');ga('send','pageview');
        </script>

        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAKzopAw5azXvb6Y64q5AlReWlDGvce3Kc&callback=myMap"></script>-->
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
    <script src="../../dist/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! -->
    <script src="../../assets/js/vendor/holder.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    </div>
    </div>
    </body>
</html>

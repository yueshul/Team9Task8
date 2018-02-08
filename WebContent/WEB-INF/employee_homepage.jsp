<!-- **********************************************************************************************************************************************************
      MAIN CONTENT
      *********************************************************************************************************************************************************** -->
         <!-- TESTSTETSTSTSTS -->   
<link rel="stylesheet" href="assets/css/bootstrap.css">
<link rel="stylesheet" href="assets/css/style.css">
<link rel="stylesheet" href="assets/css/style-responsive.css">
<link rel="stylesheet" href="assets/css/table-responsive.css">
      <jsp:include page="employee_template.jsp"></jsp:include>
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper site-min-height">
          	<h3><i class="fa fa-angle-right"></i> Discover Our Panels</h3>
          	<div class="row mt">
          		<div class="col-lg-12">
					<div class="row">
						<!-- TWITTER PANEL -->
					<div class="col-lg-4 col-md-4 col-sm-4 mb">
						<div class="darkblue-panel">
							<div class="darkblue-header {
">
								<h5>Manage My Account</h5>
							</div>
							<div class="row">
								<i class="fa fa-twitter fa-4x"></i>
								<p>
									<img src="assets/img/ui-zac.jpg" class="img-circle" width="50">
								</p>
								<p>
									<b>${employee.userName}</b>
								</p>

								<a href="employee_change_pwd.do" style="text-align:center; font-size:180%">Change
											Password</a>
											<br/>
											
											<p><br/><br/><br/></p>
												
								</div>
							</div>
						</div>
					
					<!-- /col-md-4 -->

					<div class="col-lg-4 col-md-4 col-sm-4 mb">
							<!-- WHITE PANEL - TOP USER -->
							<div class="white-panel pn">
								<div class="white-header">
									<h5>Manage Customer</h5>
								</div>
								<!-- <p><img src="assets/img/ui-zac.jpg" class="img-circle" width="50"></p> -->
								<%-- <p><b>${employee.userName}</b></p> --%>
									
									<div class="row">
										<div class="col-md-6">
											<a href="CreateCustomerAccount.do""><p style="font-size:140%; class="small mt">Create New Customers</p></a>
										</div>
								<div class="col-md-6">
											<a href="ManageCustomer.do"><p style="font-size:140%; class="small mt">View Customers</p></a>
										</div>
							</div>
							</div>
						</div><!-- /col-md-4 -->

					<div class="col-lg-4 col-md-4 col-sm-4 mb">

						<div class="white-panel pn">
							<div class="white-header">
								<h5>Manage Employee</h5>
							</div>
							
							
							<div class="row">
								<div class="col-md-6">
									<a href="ManageEmployee.do">
										<span style="font-size: 140%;" class="small mt">Manage
											Employees</span>
									</a>
								</div>
								<div class="col-md-6">
									<a href="CreateEmployeeAccount.do"><span
											style="font-size: 140%;" class="small mt">Create Employee</span></a>
								</div>

							</div>
						</div>
					</div>
					<!-- /col-md-4 -->
					</div>
					
					
					
					<div class="row">

						<div class="col-md-6 mb">
							<div class="weather pn">
								<i class="fa fa-cloud fa-4x"></i>
								<a href="depositCheck.do"><span
											style="font-size: 180%; color:white;" >Deposit Check</span></a>
								
							</div>
						</div>
						
						<div class="col-md-6 mb">
							<div class="weather-3 pn centered">
								<i class="fa fa-sun-o"></i>
								<a href="CreateFund.do"><span
											style="font-size: 180%; color:white;" >Create Fund</span></a>
								<div class="info">
									<div class="row">
										
										
										
										
										
											<!-- <h3 class="centered">MADRID</h3> -->
										<!-- <div class="col-sm-6 col-xs-6 pull-left">
											<p class="goleft"><i class="fa fa-tint"></i> 13%</p>
										</div>
										<div class="col-sm-6 col-xs-6 pull-right">
											<p class="goright"><i class="fa fa-flag"></i> 15 MPH</p>
										</div> -->
									</div>
								</div>
							</div>
						</div>
						

					</div>
					</div>
          	</div>
			
		</section>
      </section><!-- /MAIN CONTENT -->

      <!--main content end-->

    <!-- js placed at the end of the document so the pages load faster -->
    <script src="assets/js/jquery.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script class="include" type="text/javascript" src="assets/js/jquery.dcjqaccordion.2.7.js"></script>
    <script src="assets/js/jquery.scrollTo.min.js"></script>
    <script src="assets/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script src="assets/js/jquery.sparkline.js"></script>

    <!--common script for all pages-->
    <script src="assets/js/common-scripts.js"></script>

    <!--script for this page-->
    <script src="assets/js/sparkline-chart.js"></script>    
    
    
  <script>
      //custom select box

      $(function(){
          $('select.styled').customSelect();
      });

  </script>

  </body>
</html>
					
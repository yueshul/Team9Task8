<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Dashboard">
<meta name="keyword" content="Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">

<title>Carnegie Financial Service</title>

<!-- Bootstrap core CSS -->
<link href="assets/css/bootstrap.css" rel="stylesheet">
<!--external css-->
<link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet" />

<!-- Custom styles for this template -->
<link href="assets/css/style.css" rel="stylesheet">
<link href="assets/css/style-responsive.css" rel="stylesheet">

</head>
  <body>

      <!-- **********************************************************************************************************************************************************
      MAIN CONTENT
      *********************************************************************************************************************************************************** -->
	  <div id="login-page">
	  	<div class="container">	  	
		      <form method="POST" class="form-login" action="login.do">
		        <h2 class="form-login-heading">sign in now</h2>
		        <h4 style="color: red">${message}</h4>
		        <c:forEach var="errors" items="${errors}">
					<h3 style="color: red">${error}</h3>
				</c:forEach>
			    <div class="login-wrap">	
			    		<center>	            
						<table>
							<c:forEach var="field" items="${form.visibleFields}">
								<tr><td style="color: red">${field.error}</td></tr>
							</c:forEach>
							
							<c:forEach var="field" items="${form.visibleFields}">
							<c:choose>
								<c:when test="${field.name eq 'type'}">
									<tr>	
										<td style="text-align: center" colspan="1">
											<select name="type" class="form-control" required>
												<option value="Customer">Customer</option>
												<option value="Employee">Employee</option>
											</select>
										</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td style="text-align: center" colspan="1"><input class="form-control" autofocus type="${field.type}"
											name="${field.name}" value="${field.value}" placeholder="${field.label}"/></td>
									</tr>
								</c:otherwise>
							</c:choose>
								
							</c:forEach>				
						</table>
						<br/>
						<button style="width:170px;text-align:center;" class="btn btn-theme btn-block" 
						type="submit" name="action" value="Login">Next</button>	
					</center>
					<br/>

	                <br/>	            
			        <hr>
				</div>
			</form>
		</div>
        <!-- Modal -->
        <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Forgot Password ?</h4>
                    </div>
                  <div class="modal-body"> 
                   <h4 style="color: red">${message}</h4>

		        <c:forEach var="errors" items="${errors}">
				<h3 style="color: red">${error}</h3>
				</c:forEach>
                  <form method="POST" action="customerResetPwd.do">
                        <p>Enter these fields below to reset your password.</p>
                        <center>
	                        <table>
								<c:forEach var="field" items="${form.visibleFields}">
									<tr>
									<td style="font-size: x-large"><label>${field.label}</label></td>
									<td><input id="${field.name}" autocomplete="off" class = "form-control" type="${field.type}"
									name="${field.name}" value="${field.value}" /></td>
									<td style="color: red">${field.error}</td>
									</tr>
								</c:forEach>
								<tr>
									<td colspan="2" style="text-align: center;"><input
									type="submit" class="btn btn-lg btn-info-outline" name="action"
									value="CustomerResetPwd" />
									<button data-dismiss="modal" class="btn btn-default" type="button" name="action" value="cancel">Cancel</button>
									</td>
								</tr>
						  </table>
					  </center>
                        <input type="text" name="userName" placeholder="username" 
                        autocomplete="off" class = "form-control" width="10"> <br/>
                         <br/>
                        <label>First Name</label>
                        <input type="text" name="FirstName" placeholder="First Name" 
                        autocomplete="off" class = "form-control" width="10"> <br/>
                        <br/>
                         <label>Last Name</label>
                        <input type="text" name="LastName" placeholder="Last Name" 
                        autocomplete="off" class = "form-control" width="10"> <br/>
                        <br/>
                        <button data-dismiss="modal" class="btn btn-default" type="button" >Cancel</button>
                        <button class="btn btn-theme" type="button">Submit</button>
                    </form>
                </div> 
                <div class="modal-footer">
                      </div>
            </div>
        </div>
        <!-- modal -->
	  	</div>
	  </div>
    <!-- js placed at the end of the document so the pages load faster -->
    <script src="assets/js/jquery.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>

    <!--BACKSTRETCH-->
    <!-- You can use an image of whatever size. This script will stretch to fit in any screen size.-->
    <script type="text/javascript" src="assets/js/jquery.backstretch.min.js"></script>
    <script>
        $.backstretch("assets/img/login-bg.jpg", {speed: 500});
    </script>
  </body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Employee Account</title>
</head>
<body>
	<section id="container" >
		<jsp:include page="employee_template.jsp"/>     
      <!-- **********************************************************************************************************************************************************
      MAIN CONTENT
      *********************************************************************************************************************************************************** -->
      <!--main content start-->
     <section id="main-content">
          <section class="wrapper">
            				<div class="col-lg-9">
                				<div class="form-panel" style="padding:30px;margin:30px;">
                				<center><h2>Create a employee</h2></center>
                				<div id="resetMessage" style="text-align:center;">${message}</div><br/>
							<form class="form-horizontal" action="CreateEmployeeAccount.do" method="POST">
										<span class="block input-icon input-icon-right align-center">
											<c:forEach var="error" items="${errors}">
													<h7 style="color:red"> ${error} </h7>
													<br/>
											</c:forEach>
										</span><br>
									<div class="form-group">
										<label class="col-sm-2 control-label"> User Name *</label>
										<div class="col-sm-9 span3">
											<input type="text" name="userName" class="form-control" required>
										</div>
									</div>
									<br><br>
				
									<div class="form-group">
										<label class="col-sm-2 control-label"> Password *</label>
				
										<div class="col-sm-9 span3">
											<input type="password" name="password" class="form-control" required>
										</div>
									</div>
									<br><br>
									<div class="form-group">
										<label class="col-sm-2 control-label"> First Name *</label>
				
										<div class="col-sm-9 span3">
											<input type="text" name="firstName" class="form-control" required>
										</div>
									</div>
									<br><br>
									<div class="form-group">
										<label class="col-sm-2 control-label"> Last Name *</label>				
										<div class="col-sm-9 span3">
											<input type="text" name="lastName" class="form-control" required>
										</div>
									</div>
									<br><br><br>
									<div class="span2"></div>
									<center>
									<button class="btn btn-info" type="submit">
										Submit
									</button>
									</center>
								</form>
								</div>
							</div>
						</div>
					</div>
    		  </section>
     </section>
  </section>
</body>
</html>
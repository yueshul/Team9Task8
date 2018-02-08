<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/myfun.tld"  prefix="myfun" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage Employee Account</title>
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
		<div class="col-lg-11">		
			<div class="form-panel" style="padding:30px;">
					<h3>
						Employees List
					</h3>
					<div id="resetMessage" style="text-align:center;">${message}</div><br/>
					<div class="widget-body">
						<div class="widget-main no-padding">
							<table class="table table-striped table-bordered">
								<thead class="thin-border-bottom">
									<tr>
										<th>Username</th>
										<th>Full Name</th>
										<th>Reset Password</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="employee" items="${employees}">
										<tr>
											<td>${employee.userName}</td>
											<td>${employee.firstName} ${employee.lastName}</td>
											<td>
												<a href="ManageEmployee.do?resetUserName=${employee.userName}" class="btn btn-minier btn-warning">
													Reset
												</a>
											</td>
									</c:forEach>
								</tbody>
							</table>
							<div>
								<div></div>
								<center>
									<a class="btn btn-info " href="CreateEmployeeAccount.do">
										Create Employee
									</a>
								</center>
							</div>
						</div>
				</div>
			</div>
		</div>
	</section> 
	</section> 
  </section>
</body>
</html>
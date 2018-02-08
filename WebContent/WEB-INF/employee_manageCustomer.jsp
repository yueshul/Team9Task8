<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/myfun.tld"  prefix="myfun" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage Customer Account</title>
</head>
<body>
	<section id="container"> 
	<jsp:include page="employee_template.jsp" /> 
	<!-- **********************************************************************************************************************************************************
      MAIN CONTENT
      *********************************************************************************************************************************************************** -->
	<!--main content start--> 
	<section id="main-content"> 
	<section class="wrapper">
		<div class="col-lg-11">		
			<div class="form-panel" style="padding:30px;">
					<h3>
						Customers List
					</h3>
					<div id="resetMessage" style="text-align:center;">${message}</div><br/>
					<div class="widget-body">
						<div class="widget-main no-padding">
							<table class="table table-striped table-bordered">
								<thead class="thin-border-bottom">
									<tr>
										<th style="text-align:center">Username</th>
										<th style="text-align:center">Full Name</th>
										<th style="text-align:right">Cash</th>
										<th style="text-align:center">Deposit Check</th>
										<th style="text-align:center">View Account</th>
										<th style="text-align:center">Reset Password</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="customer" items="${customers}">
										<tr>
											<td style="text-align:center">${customer.userName}</td>
											<td style="text-align:center">${customer.firstName} ${customer.lastName}</td>
											<td style="text-align:right" class="align-right">${myfun:formatCashNumber(customer.cash)}</td>
											<td style="text-align:center" class="align-right"><a href="depositCheck.do?customerName=${customer.userName}">deposit
													check </a></td>
											<td style="text-align:center" class="align-right"><a
												href="viewCustomer.do?userName=${customer.userName}">View
													Account</a></td>
											<td style="text-align:center">
												<a href="ManageCustomer.do?resetUserName=${customer.userName}" class="btn btn-minier btn-warning">
													Reset
												</a>
											</td>
									</c:forEach>
								</tbody>
							</table>
							<div>
								<div></div>
								<center>
									<a class="btn btn-info " href="CreateCustomerAccount.do">
										Create Customer
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
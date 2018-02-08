<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/myfun.tld"  prefix="myfun" %>  	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Request Check</title>
<link rel="stylesheet" href="assets/css/bootstrap.css">
<link rel="stylesheet" href="assets/css/style.css">
<link rel="stylesheet" href="assets/css/style-responsive.css">
<link rel="stylesheet" href="assets/css/table-responsive.css">
</head>
<body>
	<jsp:include page="customer_template.jsp"></jsp:include>

	<section id="main-content"> 
	<section class="wrapper">
	<div class="col-lg-12">
			<div class="form-panel" style="padding:30px">
			
				<h4 style="color: red">${message}</h4>
				<h3>Request Check</h3>
			<form class="form-horizontal" method="POST" action="requestCheck.do">
				<div class="form-group">
				<label class="col-sm-2 control-label">Current
					Balance:</label>
				<p>${myfun:formatCashNumber(customer.cash)}</p>
				</div>
				<!-- <span class="block input-icon input-icon-right align-center"> -->
					<c:forEach var="error" items="${errors}">
						<h3 style="color: red">${error}</h3>
					</c:forEach>
					<c:forEach var="error" items="${form.formErrors}">
						<h3 style="color:red" class="col-sm-12"> ${error} </h3>
						</c:forEach>
				<div class="form-group">
					<label class="col-sm-2 control-label"> Check Amount </label>
					<div class="col-sm-9 span3">
						<input type="text" name="checkAmount" class="form-control"
							value="" placeholder="please input your requesting check amount">
					</div>
				</div>
				<center>
				<button class="btn btn-info" type="submit" name="action"
					value="SubmitRequest">
					<i class="icon-ok bigger-110"></i> Request Check
				</button>
				</center>
			</form><br/>
			<table style="width:75%;margin:auto" class="table table-hover">
			<thead style="font-family:monospace; font-size:180%">
				<tr><th> Status</th><th> Amount</th><th>Type</th><th> Date</th></tr>
			</thead>
			<c:forEach var="transaction" items="${transactions}">
				<tbody style="width:80%">
							<c:choose>
								<c:when test="${(transaction.userName == customer.userName) and (transaction.transactionType == 'Request Check')}">                                  
									<c:choose>
										<c:when test="${transaction.executeDate != Null}">
									
									<tr><td>${transaction.status}</td><td>${myfun:formatCashNumber(transaction.amount)}</td><td>${transaction.transactionType}</td><td>${myfun:formatDate(transaction.executeDate)}</td>
										</tr>
										</c:when>
										<c:otherwise>
										<tr><td>${transaction.status}</td><td>${myfun:formatCashNumber(transaction.amount)}</td><td>${transaction.transactionType}</td><td> </td></tr>
									</c:otherwise>
									
									</c:choose>		
											</c:when>
											<c:otherwise>
										</c:otherwise>
										</c:choose>
									
									</tbody>
									</c:forEach>
							</table>	
				</div>
				</div>
			</section>
</section> 
</body>
</html>
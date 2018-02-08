<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="assets/css/bootstrap.css">
<link rel="stylesheet" href="assets/css/style.css">
<link rel="stylesheet" href="assets/css/style-responsive.css">
<link rel="stylesheet" href="assets/css/table-responsive.css">
<title>Change Password</title>
</head>
<body>
	<jsp:include page="customer_template.jsp"></jsp:include>

	<section id="main-content"> <section class="wrapper">

	<div class="row mt">
		<div class="col-lg-12">
			<div class="form-panel" style="padding:30px">	
				<h3>Enter your new password</h3>
				<form class="form-horizontal" method="POST"
					action="customer_change_pwd.do">
					<!-- <span class="block input-icon input-icon-right align-center"> -->
						<c:forEach var="error" items="${errors}">
							<h4 style="color: red">${error}</h4>
						</c:forEach>
						<c:forEach var="error" items="${form.formErrors}">
						<h3 style="color:red" class="col-sm-12"> ${error} </h3>
						</c:forEach>
							<br>

					<div class="form-group">
						<label class="col-sm-3 control-label"> Old Password </label>
						<div class="col-sm-8 span3">
							<input type="password" name="oldPassword" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label"> New Password </label>
						<div class="col-sm-8 span3">
							<input type="password" name="newPassword" class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label"> Confirm New
							Password </label>
						<div class="col-sm-8 span3">
							<input type="password" name="confirmPassword" class="form-control">
						</div>
					</div>
					<div class="span2"></div>
					<center>
						<button class="btn btn-info" type="submit" name="action" value="ChangePassword">
							Submit
						</button>
					</center>
				</form>
			</div>
			</div>
			</div>
	</section> </section>
</body>
</html>


<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Fund</title>
</head>
<body>
	<section id="container"> <jsp:include
		page="employee_template.jsp" /> <!-- **********************************************************************************************************************************************************
      MAIN CONTENT
      *********************************************************************************************************************************************************** -->
	<!--main content start--> <section id="main-content"> <section
		class="wrapper">
		<div class="col-lg-11">
			<div class="form-panel" style="padding:30px;">
				<h3>Create New Fund</h3>
				<div id="resetMessage" style="text-align:center;">${message}</div><br/>
				<form class="form-horizontal" action="CreateFund.do" method="POST">
					<span class="block input-icon input-icon-right align-center">
						<c:forEach var="error" items="${errors}">
							<h6 style="color:red"> ${error} </h6>
							<br />
						</c:forEach>
					</span><br>
					<div class="form-group">
						<label class="col-sm-2 control-label"> Fund Name </label>
						<div class="col-sm-9 span3">
							<input type="text" name="name" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"> Fund Ticker </label>
						<div class="col-sm-9 span3">
							<input type="text" name="symbol" class="form-control">
						</div>
					</div>
					<div class="span2"></div>
						<center>
							<button class="btn btn-info" type="submit">
								<i class="icon-ok bigger-110"></i> Submit
							</button>
						</center>
				</form>
			</div>
		</div>
	</section> </section> </section>
</body>
</html>
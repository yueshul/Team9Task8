<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Deposit Check</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/blog.css">
</head>
<body>
	
	<jsp:include page="employee_template.jsp" />
	<section id="main-content"> <section class="wrapper">
	<div class="row mt">
		<div class="col-lg-12">
			<div class="form-panel">
				<h4 style="color: red">${message}</h4>
				<h3>Deposit Check</h3>
				<form class="form-horizontal" method="POST" action="depositCheck.do">
					<!-- <span class="block input-icon input-icon-right align-center"> -->
						<c:forEach var="error" items="${errors}">
							<h3 style="color: red">${error}</h3>
						</c:forEach>
						<c:forEach var="error" items="${form.formErrors}">
						<h3 style="color:red" class="col-sm-12"> ${error} </h3>
						</c:forEach>
					<div class="form-group">
						<label class="col-sm-2 control-label"> Customer User Name
						</label>
						<div class="col-sm-9 span3">
							<input id="customerNameField" type="text" name="customerUserName" value="${customerName}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">Check Amount</label>
						<div class="col-sm-9 span3">
							<input type="text" name="checkAmount" class="form-control" >
						</div>
					</div>
					<button class="btn btn-info" type="submit" name="action" value="SubmitRequest">
						<i class="icon-ok bigger-110"></i> Deposit Check
					</button>
					
				</form>
			</div>
		</div>
	</section> </section>
	</section>
	<!-- <script type="text/javascript">
		var inputField = document.getElementById("customerNameField");
		console.log(inputField);
		if(inputField.value.length == 0){
			console.log("value null");
			inputField.removeAttribute("disabled");
		}
	</script> -->
</body>
</html>

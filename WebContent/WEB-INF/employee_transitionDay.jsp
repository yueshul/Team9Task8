<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/myfun.tld"  prefix="myfun" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Transition Day</title>
</head>
<body>
	<section id="container" >
		<jsp:include page="employee_template.jsp"/>
      <!--main content start-->
     <section id="main-content">
          <section class="wrapper">
            				<div class="col-lg-12">
                				<div class="form-panel" style="padding:30px;margin:30px;">
                				<c:forEach var="error" items="${errors}">
					 			<h4 style="color:red"> ${error} </h4><br/>
							</c:forEach>
                				<form method="POST">
                				<h2>Please input latest prices for funds and start transition day</h2><br/>
                				<h4 style="color:red"> ${message} </h4><br/>
                                Date: <input name="newDate" type="date" value="${myfun:formatDate(lastTradingDay)}" min="${myfun:formatDate(lastTradingDay)}" required>
									<table class="table table-striped table-bordered" style="margin:40px;width:90%">
										<thead>
											<tr>
												<th>Ticker</th>
												<th>Name</th>
												<th>Price</th>
											</tr>
										</thead>
										<tbody>										
										<c:forEach var="fund" items="${funds}" >
											<tr>
											<td>
											<c:out value="${fund.getSymbol()}" />	
											</td>
												<td>
												<c:out value="${fund.getName()}" />	
												</td>										
												<td>
												$ <input name="price-${fund.getSymbol()}" value="${myfun:formatPriceNumber(fund.getLatestPrice())}" type="text"  required>
												</td>										
											</tr>
										</c:forEach>
										</tbody>
									</table>
								<center>
									<button class="btn btn-info" type="submit">
										<i class="icon-ok bigger-110"></i>
										Start Transition Day
									</button>
								</center>
						       </form>	
								</div>
						</div>
    		  </section>
     </section>
  </section>
	
</body>
</html>
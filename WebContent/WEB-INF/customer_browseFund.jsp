<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/myfun.tld" prefix="myfun"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="assets/css/bootstrap.css">
<link rel="stylesheet" href="assets/css/style.css">
<link rel="stylesheet" href="assets/css/style-responsive.css">
<link rel="stylesheet" href="assets/css/table-responsive.css">
</head>
<body>
	<jsp:include page="customer_template.jsp"></jsp:include>
	<section id="main-content"> <section class="wrapper">
	<div id="morris">
		<div class="row mt">
			<!--start of title  -->
			<div class="col-lg-12">
				<div class="row mt">
					<div class="col-lg-6">
						<h3>
							<i class="fa fa-angle-right"></i>Research Funds
						</h3>
					</div>
				</div>
			</div>
			<!-- start of search bar -->

			<div class="row">
				<div class="col-lg-12">
					<div class="form-panel" style="padding:30px">
							<c:forEach var="error" items="${errors}">
								<h4 style="color: red">${error}</h4>
							</c:forEach>
							<form class="search" method="POST" action="researchFund.do">
								<div class="form-group">
									Search by name: <input type="text" name="name"
										placeholder="please enter fund name" class="form-control" />
								</div>
								<div class="form-group">
									Search by symbol: <input type="text" name="symbol"
										placeholder="please enter fund symbol" class="form-control" />
								</div>
								<div class="form-group">
									<center>
									<input type="submit" name="action" value="Search"
										class="btn btn-info" /></center>
								</div>
							</form>
						<!-- start of fund lists -->
						<div class="row" style="margin-left: 0px; margin-right: 0px">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>Fund Name</th>
											<th>Fund Ticker</th>
											<th>Description</th>
											<th style="text-align: right">Volume</th>
											<th style="text-align: right">Last Price</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="fund" items="${funds}">

											<tr>
												<td>${fund.name}</td>
												<td>${fund.symbol}</td>
												<c:choose>
													<c:when test="${empty fund}">
														<td>${""}</td>
													</c:when>
													<c:otherwise>
														<td>To be found in detail<%-- ${fund.description} --%></td>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${empty fund}">
														<td>${""}</td>
													</c:when>
													<c:otherwise>

														<td style="text-align: right">2,234,500<!--fund.volume  --></td>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${fund.latestPrice==0}">
														<td>${""}</td>
													</c:when>
													<c:otherwise>
														<td style="text-align: right">${myfun:formatCashNumber(fund.latestPrice)}</td>
													</c:otherwise>
												</c:choose>
												<td style="text-align: center"><a
													href="researchFund.do?fundName=${fund.name}"
													class="btn btn-minier btn-warning">View Detail</a></td>
											</tr>

										</c:forEach>
									</tbody>
								</table>
						</div>
						<!--content-panel -->
					</div>
				</div>
				<!-- /col-md-12 -->
			</div>
			<!-- row -->
		</div>
	</div>
	</section> </section>
	<!-- /MAIN CONTENT -->

</body>
</html>

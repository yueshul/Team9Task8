<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/myfun.tld" prefix="myfun"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="assets/css/bootstrap.css">
<link rel="stylesheet" href="assets/css/style.css">
<link rel="stylesheet" href="assets/css/style-responsive.css">
<link rel="stylesheet" href="assets/css/table-responsive.css">
</head>
<body>
	<jsp:include page="customer_template.jsp"></jsp:include>
	<section id="main-content">
		<section class="wrapper">
			<div id="morris">

				<div class="form-panel" style="padding: 30px">
					<div class="row mt">
						<!--start of title  -->
						<div class="col-lg-12">
							<div class="col-lg-6">
								<h3>Statistics For ${fund.name}</h3>
							</div>
						</div>
						<!--start of chart -->
						<div class="col-lg-6">
							<div class="darkblue-panel pn">
								<div class="darkblue-header">
									<h5>Price History Chart</h5>
								</div>

								<c:choose>
									<c:when test="${empty priceHistory}">
										<div class="chart mt">
											<div class="sparkline" data-type="line" data-resize="true"
												data-height="75" data-width="90%" data-line-width="1"
												data-line-color="#fff" data-spot-color="#fff"
												data-fill-color="" data-highlight-line-color="#fff"
												data-spot-radius="4" data-data="${priceHistory}"></div>
											<p class="mt" style="color: white;">
												<b>This is a new fund, no price available.</b>
											</p>
									</c:when>
									<c:otherwise>
										<div class="chart mt">
											<div class="sparkline" data-type="line" data-resize="true"
												data-height="75" data-width="90%" data-line-width="1"
												data-line-color="#fff" data-spot-color="#fff"
												data-fill-color="" data-highlight-line-color="#fff"
												data-spot-radius="4" data-data="${priceHistory}"></div>
									</c:otherwise>
								</c:choose>







								<%-- 	<canvas width="334" height="75" style="display: inline-block; width: 334px; height: 75px; vertical-align: top;"></canvas>
							 --%>
							</div>
						</div>
					</div>
					<!-- /col-md-4 -->
					<!-- start of data -->
					<div class="col-lg-6">
						<!-- 	<form class="search" method="POST" action="researchFund.do">
						</form>
 -->
						<!-- start of fund lists -->
						<table class="table table-hover">
							<thead>
								<tr>
									<th>Ticker</th>
									<th style="text-align: right">Volume</th>
									<th style="text-align: right">Last Price</th>
									<th style="text-align: right">Price Change</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>${fund.symbol}</td>
									<td style="text-align: right">2,234,500<!--fund.volume  --></td>
									<c:choose>
										<c:when test="${empty priceHistory}">
											<td>N/A</td>
										</c:when>
										<c:otherwise>
											<td style="text-align: right">${myfun:formatCashNumber(fund.latestPrice)}</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${empty priceHistory}">
											<td>N/A</td>
										</c:when>
										<c:otherwise>
											<td style="text-align: right">${myfun:formatSharesNumber(change)}%</td>
										</c:otherwise>
									</c:choose>
								</tr>
							</tbody>
						</table>
						<div class="row" style="margin-left: 0px; margin-right: 0px">
							<div class="form-group">
								<label class="col-sm-2 col-sm-2 control-label">Description</label>
								<div class="col-sm-10">
									<input type="text" readonly="readonly" class="form-control"
										style="height: 100px"
										value="${fund.name} Inc. is an American multinational technology company headquartered in Cupertino, California that designs, develops, and sells consumer electronics, computer software, and online services." />
								</div>
							</div>
						</div>
						<table class="table table-hover">

							<tbody>
								<td><a href="browseFund.do" class="btn btn-minier btn-info">
										<i class="icon-undo"></i> Back To Search
								</a></td>
								<td class="align-right"><a
									href="buyFund.do?symbol=${fund.symbol}"
									class="btn btn-minier btn-warning">Buy Fund</a></td>
								<td class="align-right"><a
									href="sellFund.do?symbol=${fund.symbol}"
									class="btn btn-minier btn-warning">Sell Fund</a></td>

							</tbody>
							<script src="assets/js/jquery.js"></script>
							<script src="assets/js/jquery-1.8.3.min.js"></script>
							<script src="assets/js/bootstrap.min.js"></script>
							<script class="include" type="text/javascript"
								src="assets/js/jquery.dcjqaccordion.2.7.js"></script>
							<script src="assets/js/jquery.scrollTo.min.js"></script>
							<script src="assets/js/jquery.nicescroll.js"
								type="text/javascript"></script>
							<script src="assets/js/jquery.sparkline.js"></script>


							<!--common script for all pages-->
							<script src="assets/js/common-scripts.js"></script>

							<script type="text/javascript"
								src="assets/js/gritter/js/jquery.gritter.js"></script>
							<script type="text/javascript" src="assets/js/gritter-conf.js"></script>

							<!--script for this page-->
							<script src="assets/js/sparkline-chart.js"></script>
							<script src="assets/js/zabuto_calendar.js"></script>

							<br>
						</table>
					</div>
				</div>
			</div>
		<br> <br>
			</div>
			</div>
			<!-- /col-md-12 -->
			</div>
			<!-- row -->
			</div>
			</div>
		</section>
	</section>
	<!-- /MAIN CONTENT -->
</body>
</html>

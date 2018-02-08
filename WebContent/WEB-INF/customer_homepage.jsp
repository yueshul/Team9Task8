<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/myfun.tld"  prefix="myfun" %>  

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<!--start of information heading-->
	<h3>
		<i class="fa fa-angle-right"></i>My Account Information
	</h3>
	<!--start of basic information-->
	<div class="form-panel">
		<div class="row mt">
			<div class="col-lg-12">
				<form class="form-horizontal style-form" method="get">
					<div class="col-lg-6">
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">Name</label>
							<div class="col-sm-10">
								<input type="text" readonly="readonly" class="form-control"
									value="${customer.firstName} ${customer.lastName}" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">Email</label>
							<div class="col-sm-10">
								<input type="text" readonly="readonly" class="form-control"
									value=" ${customer.email}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">Balance</label>
							<div class="col-sm-10">
								<input type="text" readonly="readonly" class="form-control"
									value="${myfun:formatCashNumber(customer.cash)}">
							
							</div>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">Address</label>
							<div class="col-sm-10">
								<input type="text" readonly="readonly" class="form-control"
									value=" ${customer.addressLine1} ${customer.addressLine2}" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">State/City</label>
							<div class="col-sm-10">
								<input type="text" readonly="readonly" class="form-control"
									value="${customer.state} ${customer.city}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 col-sm-2 control-label">Zip</label>
							<div class="col-sm-10">
								<input type="text" readonly="readonly" class="form-control"
									value="${customer.zip}">
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!--end of basic information  --> <!-- start of list of funds -->
	  <div id="morris">
                  <div class="row mt">
                    <div class="col-lg-6">
                              <h3><i class="fa fa-angle-right"></i>List of Funds</h3>
                      </div>
                      <div class="col-lg-6">
                          <div class="content-panel" style="text-align: center;">
                              <h4>Last Execute Date of My Funds:${myfun:formatDate(date)}</h4>
                              <div class="panel-body">
                                  <div id="hero-bar" class="graph"></div>
                              </div>
                          </div>
                      </div>
                  </div>
                </div>
	<div class="row">
		<div class="col-md-12 mt">
			<div class="content-panel" style="padding:30px">
				<h4>
					Show <select name="entry">
						<option value="volvo">10</option>
						<option value="saab">25</option>
						<option value="fiat">50</option>
						<option value="audi">100</option>
					</select> entries
				</h4>
				<hr/>
				<table class="table table-hover" style="padding:20px">
					
					<thead>
						<tr>
							<th style="width:10%">Fund Name</th>
							<th style="text-align:right;width:15%">Latest Price</th>
							<th style="text-align:right">Shares</th>
							<th style="text-align:right">Value</th>
						</tr>
					</thead>
					<c:forEach var="funds" items="${funds}">
						<tbody>
							<tr>
								<td>${funds.getKey().name}</td>
								<td style="text-align:right">${myfun:formatCashNumber(funds.getKey().latestPrice)}
								</td>
								<td style="text-align:right">${myfun:formatSharesNumber(funds.getValue())}
								</td>	
								<td style="text-align:right">${myfun:formatCashNumber(funds.getValue() * funds.getKey().latestPrice)}</td>
								<td style="text-align:center">
								<a href="buyFund.do?symbol=${funds.getKey().symbol}" class="btn btn-info">Buy Fund</a> &nbsp;&nbsp;&nbsp;
								<a href="sellFund.do?symbol=${funds.getKey().symbol}" class="btn btn-info">Sell Fund</a></td>
							</tr>
						</tbody>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
	</section></section>
	<!-- end of list of funds -->
	<!--start of transaction-->
	<section id="main-content"> <section class="wrapper">
		  <div id="morris">
                  <div class="row mt">
                    <div class="col-lg-6">
                              <h3><i class="fa fa-angle-right"></i>Transaction History</h3>
                      </div>
                  </div>
                </div>
		<div class="col-lg-12">
			<div class="content-panel" style="padding:30px">
				<table class="table table-striped table-advance table-hover">
					<thead>
						<tr>
							<th><i class="fa fa-bullhorn"></i> Transaction Date</th>
							<th class="hidden-phone">
								Operation</th>
							<th style="text-align:center">Fund Symbol</th>
							<th style="text-align:right">Price</th>
							<th style="text-align:right">Shares</th>
							<th style="text-align:right">Amount</th>
							<th style="text-align:center">Status</th>
						</tr>
					</thead>
					<c:forEach var="transaction" items="${transactions}">
								<tbody>
									<tr>
										<td class="hidden-phone">${myfun:formatDate(transaction.executeDate)}</td>
										<td class="hidden-phone">${transaction.transactionType}</td>
										<td style="text-align:center" class="hidden-phone">${transaction.symbol}</td>
										
										<c:choose>
										<c:when test="${transaction.price==0}">
										<td style="text-align:right"><fmt:formatNumber type="number" pattern=",##0.000"
										value="" /></td>
										</c:when>
										<c:otherwise>
										<td style="text-align:right" class="hidden-phone">${myfun:formatCashNumber(transaction.price)}</td>
										</c:otherwise>
										</c:choose>
										<c:choose>
										<c:when test="${transaction.shares==0}">
										<td style="text-align:right"><fmt:formatNumber type="number" pattern=",##0.000"
										value="" /></td>
										</c:when>
										<c:otherwise>
										<td style="text-align:right" class="hidden-phone">${myfun:formatSharesNumber(transaction.shares)}</td>
										</c:otherwise>
										</c:choose>
										<c:choose>
										<c:when test="${transaction.amount==0}">
										<td style="text-align:right"><fmt:formatNumber type="number" pattern=",##0.000"
										value="" /></td>
										</c:when>
										<c:otherwise>
										 <td style="text-align:right" class="hidden-phone">${myfun:formatCashNumber(transaction.amount)}
										</td>
										</c:otherwise>
										</c:choose>
										<c:choose>
										<c:when test="${transaction.status=='Pending'}">
										<td style="text-align:center" class="hidden-phone"><span
											class="label label-warning label-mini"> <c:out
													value="${transaction.status}" />
										</span>
										</c:when>
										<c:when test="${transaction.status=='Declined'}">
										<td style="text-align:center" class="hidden-phone"><span
											class="label label-danger label-mini"> <c:out
													value="${transaction.status}" />
										</span>
										</c:when>
										<c:otherwise>
										<td style="text-align:center" class="hidden-phone"><span
											class="label label-success label-mini"> <c:out
													value="${transaction.status}" />
										</span>
										</td>
										</c:otherwise>
										</c:choose>
										  
								</tbody>
					</c:forEach>
				</table>
				</div>
			</div>
			
			<!-- /content-panel -->
		</div>
		<!-- /col-md-12 -->
	</div>

</body>
</html>
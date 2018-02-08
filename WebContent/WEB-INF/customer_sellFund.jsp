<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/myfun.tld"  prefix="myfun" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="assets/css/bootstrap.css">
<link rel="stylesheet" href="assets/css/style.css">
<link rel="stylesheet" href="assets/css/style-responsive.css">
<link rel="stylesheet" href="assets/css/table-responsive.css">
<title>Sell Fund</title>
</head>
<body>
<jsp:include page="customer_template.jsp" />
      <!-- **********************************************************************************************************************************************************
      MAIN CONTENT
      *********************************************************************************************************************************************************** -->
       <section id="main-content">
          <section class="wrapper">
           <!--  <h3><i class="fa fa-angle-right"></i>Fund</h3>
            -->             <!-- BASIC FORM ELELEMNTS -->
            <div class="row mt">
              <div class="col-lg-12">
                  <div class="form-panel" style="padding:30px">
                      <h4 class="mb">Sell Fund</h4>
                      <form action="sellFund.do" method="post" class="form-horizontal style-form">
                      <c:if test="${!(empty message)}">
						<h2 style="text-align: center;" class="col-sm-12">${message}</h2>
					 </c:if>
					 <c:forEach var="error" items="${errors}">
						<h3 style="color:red" class="col-sm-12"> ${error} </h3>
					</c:forEach>
					<c:forEach var="error" items="${form.formErrors}">
						<h3 style="color:red" class="col-sm-12"> ${error} </h3>
					</c:forEach>
					<c:forEach var="field" items="${form.visibleFields}"> 
                          <div class="form-group">
                          	  <h4 style="color:red" class="col-sm-12">${field.error}</h4>
                              <label class="col-sm-2 control-label">${field.label}</label>
                              <div class="col-sm-8">
                                  <input type="text" class="form-control" id="${field.name}" name="${field.name}" value="${field.value}">
                              </div>
                          </div>
                          </c:forEach>
                          <center><button type="submit" class="btn btn-info" name="action" value="sell">Sell Fund</button></center>
                      </form>
                      <!-- start of fund lists -->
						<div class="row" style="margin-left: 0px; margin-right: 0px" >
								<table class="table table-hover" >
									<thead>
										<tr>
											<th style="text-align:center" width="10%">Fund Ticker</th>
											<th style="text-align:right" width="20%">Number of Shares</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="fund" items="${funds}">
										
											<tr>
												<td style="text-align:center">${fund.getKey()}</td>
												<td style="text-align:right">${myfun:formatSharesNumber(fund.getValue())}</td>
											</tr>
											
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
						<!--content-panel -->
                  </div>
                 
              </div>
            
    </section>
      </section><!-- /MAIN CONTENT -->

    <!-- js placed at the end of the document so the pages load faster -->
    <script src="assets/js/jquery.js"></script>
    <script src="assets/js/jquery-1.8.3.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script class="include" type="text/javascript" src="assets/js/jquery.dcjqaccordion.2.7.js"></script>
    <script src="assets/js/jquery.scrollTo.min.js"></script>
    <script src="assets/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script src="assets/js/jquery.sparkline.js"></script>


    <!--common script for all pages-->
    <script src="assets/js/common-scripts.js"></script>
    
    <script type="text/javascript" src="assets/js/gritter/js/jquery.gritter.js"></script>
    <script type="text/javascript" src="assets/js/gritter-conf.js"></script>

    <!--script for this page-->
    <script src="assets/js/sparkline-chart.js"></script>    
	<script src="assets/js/zabuto_calendar.js"></script>	
	
	
	<script type="application/javascript">
        $(document).ready(function () {
            $("#date-popover").popover({html: true, trigger: "manual"});
            $("#date-popover").hide();
            $("#date-popover").click(function (e) {
                $(this).hide();
            });
        
            $("#my-calendar").zabuto_calendar({
                action: function () {
                    return myDateFunction(this.id, false);
                },
                action_nav: function () {
                    return myNavFunction(this.id);
                },
                ajax: {
                    url: "show_data.php?action=1",
                    modal: true
                },
                legend: [
                    {type: "text", label: "Special event", badge: "00"},
                    {type: "block", label: "Regular event", }
                ]
            });
        });
        
        
        function myNavFunction(id) {
            $("#date-popover").hide();
            var nav = $("#" + id).data("navigation");
            var to = $("#" + id).data("to");
            console.log('nav ' + nav + ' to: ' + to.month + '/' + to.year);
        }
    </script>
  

  </body>
</html>

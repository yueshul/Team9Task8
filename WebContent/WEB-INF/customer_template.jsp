<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Dashboard">
<meta name="keyword"
	content="Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">

<title>Dashboard</title>

<!-- Bootstrap core CSS -->
<link href="assets/css/bootstrap.css" rel="stylesheet">

<link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css"
	href="assets/css/zabuto_calendar.css">
<link rel="stylesheet" type="text/css"
	href="assets/js/gritter/css/jquery.gritter.css" />
<link rel="stylesheet" type="text/css" href="assets/lineicons/style.css">

<!-- Custom styles for this template -->
<link href="assets/css/style.css" rel="stylesheet">
<link href="assets/css/style-responsive.css" rel="stylesheet">

<script src="assets/js/chart-master/Chart.js"></script>
<title>Title</title>
</head>
<body>
	<header class="header black-bg">
	<div class="sidebar-toggle-box">
	</div>
	<!--logo start--> <a href="login.do" class="logo"><b>Mutual
			Fund</b></a> <!--logo end-->
	<div class="top-menu">
		<ul class="nav pull-right top-menu">
			<li><a class="logout" href="logout.do">Logout</a></li>
		</ul>
	</div>
	</header>
	<!--header end-->

	<!-- **********************************************************************************************************************************************************
    MAIN SIDEBAR MENU
    *********************************************************************************************************************************************************** -->
	<!--sidebar start-->
	<aside>
	<div id="sidebar" class="nav-collapse ">
		<!-- sidebar menu start-->
		<ul class="sidebar-menu" id="nav-accordion">

			<p class="centered">
				<img src="assets/img/ui-sam.jpg"
					class="img-circle" width="60"></a>
			</p>
			<h5 class="centered">${customer.userName}</h5>

			<li class="mt"><a href="viewAccount.do"> <i
					class="fa fa-dashboard"></i> <span>Dashboard</span>
			</a></li>
			<li class="sub-menu"><a href="customer_change_pwd.do"> <i
					class="fa fa-th"></i> <span>Change Password</span>
			</a></li>
			<li class="sub-menu"><a href="requestCheck.do"> <i
					class="fa fa-money"></i> <span>Request Check</span>
			</a></li>
			<li class="sub-menu"><a href="buyFund.do"> <i
					class="fa fa-institution"></i> <span>Buy Fund</span>
			</a></li>
			<li class="sub-menu"><a href="sellFund.do"> <i
					class="fa fa-credit-card"></i> <span>Sell Fund</span>
			</a></li>
			<li class="sub-menu"><a href="browseFund.do"> <i
					class="fa fa-credit-card"></i> <span>Research Fund</span>
			</a></li>
		</ul>
		<!-- sidebar menu end-->
	</div>
	</aside>
	<!--sidebar end-->
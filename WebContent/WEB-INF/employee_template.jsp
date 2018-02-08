<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Dashboard</title>
<link rel="stylesheet" href="assets/css/bootstrap.css">
<link rel="stylesheet" href="assets/css/style.css">
<link rel="stylesheet" href="assets/css/style-responsive.css">
<link rel="stylesheet" href="assets/css/table-responsive.css">

</head>
<body>
	<header class="header black-bg">
	    <div class="sidebar-toggle-box">
	        <div class="fa fa-bars tooltips" data-placement="right" data-original-title="Toggle Navigation"></div>
	    </div>
	    <!--logo start-->
	    <a href="login.do" class="logo"><b>Mutual Fund</b></a>
	    <!--logo end-->
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
    <aside th:fragment="sidebar">
        <div id="sidebar"  class="nav-collapse ">
            <!-- sidebar menu start-->
            <ul class="sidebar-menu" id="nav-accordion">          
            	  <p class="centered"><a id="profile" href="login.do" ><img src="assets/img/ui-sam.jpg" class="img-circle" width="60"/></a></p>
            	  <h5 class="centered">${employee.userName}</h5>            	  
                <li class="mt">
                    <a id="dashboard" href="login.do">
                        <i class="fa fa-dashboard"></i>
                        <span>Dashboard</span>
                    </a>
                </li>

                <li class="sub-menu">
                    <a id="account" href="employee_change_pwd.do" >
                        <i class="fa fa-credit-card"></i>
                        <span>Change Password</span>
                    </a>
                </li>

                <li class="sub-menu">
                    <a id="bills" href="ManageCustomer.do" >
                        <i class="fa fa-cogs"></i>
                        <span>Manage Customer</span>
                    </a>
                </li>
                <li class="sub-menu">
                    <a id="transfermoney" href="ManageEmployee.do" >
                        <i class="fa fa-book"></i>
                        <span>Manage Employee</span>
                    </a>
                </li>
                <!-- <li class="sub-menu">
                    <a id="loan_" href="depositCheck.do" >
                        <i class="fa fa-institution"></i>
                        <span>Deposit Check</span>
                    </a>
                </li> -->
                <li class="sub-menu">
                    <a id="loan_" href="CreateFund.do" >
                        <i class="fa fa-institution"></i>
                        <span>Create Fund</span>
                    </a>
                </li>
                <li class="sub-menu">
                    <a id="loan_" href="TransitionDay.do" >
                        <i class="fa fa-institution"></i>
                        <span>Transition Day</span>
                    </a>
                </li>
            </ul>
            <!-- sidebar menu end-->
        </div>
    </aside>
    <!--sidebar end-->


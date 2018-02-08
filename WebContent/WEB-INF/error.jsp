<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>MutualFund -- Error Page</title>
    </head>
    
	<body>
	
		<h2>Mutual Fund Error</h2>
 <c:if test="${!(empty error)}">
            <h3 style="color:red"> ${error} </h3>
        </c:if>
        
        <c:forEach var="error" items="${errors}">
		<p style="color: red">${error}</p>
	</c:forEach>
        
<c:choose>
			<c:when test="${ (empty user) }">
				Click <a href="login.do">here</a> to login.
			</c:when>
			<c:otherwise>
				Click <a href="viewCustomer.do">here</a> to return to the Dashboard.
			</c:otherwise>
		</c:choose>
	</body>
</html>
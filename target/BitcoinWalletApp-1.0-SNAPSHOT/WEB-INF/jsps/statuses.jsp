<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link href="${pageContext.request.contextPath}/static/css/main.css" rel="stylesheet" type="text/css"/>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>List Of The Statuses</title>
</head>
<body>

<table class="status" align="center">

    <h2 class="headertekst" style="color:black">TRANSACTION STATUSES</h2>
    </br>
    </br>

    <tr>
        <td>User Id</td>
        <td>Balance (BTC)</td>
        <td>Transaction</td>
        <td>Address</td>
    </tr>

    <c:forEach var="status" items="${statuses}">
        <tr>
            <td><c:out value="${status.user_id}"></c:out></td>
            <td><c:out value="${status.balance}"></c:out></td>
            <td><c:out value="${status.transaction}"></c:out></td>
            <td><c:out value="${status.address}"></c:out></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
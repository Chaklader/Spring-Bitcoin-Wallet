<%@ page import="mobi.puut.controllers.WalletModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="mobi.puut.controllers.WalletModel" %>
<%@ page import="java.text.DecimalFormat" %>
<html>
<head>
    <title>Balance</title>
    <link href="${pageContext.request.contextPath}/static/css/app.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.js"
            integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/static/js/app.js"></script>
</head>

<%
    WalletModel walletModel = (WalletModel) request.getAttribute("walletModel");
    DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
%>
<body class="page_container">
<div class="wallets_page">

    <div class="top_section">

        <div class="balance_box">
            <div class="balance_row">
                <div class="fild_label">Balance</div>
                <div class="fild_value">
                <%= decimalFormat.format(walletModel.getBalanceFloatFormat()) %>&nbsp; BTC
                </div>
            </div>
            </br>
            <div class="address_row">
                <%= walletModel.getAddress() != null ? walletModel.getAddress().toString() : "Getting the address ..."%>
            </div>
        </div>
    </div>
</div>
</body>


<script>
    <% if(walletModel.getAddress() == null) {%>
    setTimeout(function () {
        window.location.reload(1);
    }, 3000);
    <% }%>

    setInterval(function () {
        getBalanceAndRefreshPageIfNotEqual('${wallet_id}', '<%= walletModel.getBalanceFloatFormat() %>');
    }, 5000);
</script>
</html>

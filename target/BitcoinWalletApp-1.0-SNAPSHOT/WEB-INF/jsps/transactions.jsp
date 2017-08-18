<%@ page import="mobi.puut.controllers.WalletModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="org.bitcoinj.core.Transaction" %>

<html>
<head>
    <title>Transactions</title>
    <link href="${pageContext.request.contextPath}/static/css/app.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.js"
            integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE="
            crossorigin="anonymous"></script>
</head>



<%
    WalletModel walletModel = (WalletModel) request.getAttribute("walletModel");
    List<Transaction> transactions = walletModel.getTransactions();
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

    <div class="history_section">
        <% if (!walletModel.isSyncFinished() && !transactions.isEmpty()) {%>
        Getting the transaction info ...
        <% } else if (walletModel.isSyncFinished() && transactions.isEmpty()) {%>
        No transaction recorded yet!!
        <% } else {
            for (Transaction transaction : transactions) {
        %>
        <p><%= walletModel.addTransactionHistory(transaction)%>
        </p>
        <% }
        }%>
    </div>


    <div class="status_line">
        <%=  walletModel.isSyncFinished() ? "Synchronized" : "Synchronizing to the block chain ..."%>
    </div>
</div>

</body>

<script>
    <% if(!walletModel.isSyncFinished()) {%>
    setTimeout(function () {
        window.location.reload(1);
    }, 5000);
    <% } else { %>
    setInterval(function () {
        getTransactionsNumberAndRefreshPageIfNotEqual('${wallet_id}', '<%= transactions.size() %>');
    }, 5000);
    <% }%>
</script>
</html>

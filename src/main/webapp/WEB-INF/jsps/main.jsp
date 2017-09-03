<%@ page import="mobi.puut.entities.WalletInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="org.omg.CORBA.Object" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <title>Main</title>

    <%--CSS sources--%>
    <link href="${pageContext.request.contextPath}/static/css/app.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <%--JavaScript sources--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.js"
            integrity="sha256-DZAnKJ/6XZ9si04Hgrsxu/8s717jcIzLy3oi35EouyE=" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/static/js/app.js"></script>
</head>
<%
    List<WalletInfo> wallets = (List<WalletInfo>) request.getAttribute("wallets");
%>

<body class="page_container">
<div class="wallets_page">

    <form id="mnfrm" action="/" method="get" target="_blank">
        <div class="buttons_box">

            <button id="genAddress" type="button" class="btn btn-default btn-lg active">
                Generate address
            </button>

            <button type="submit"
                    class="btn btn-default btn-lg active" <%= wallets == null || wallets.isEmpty() ? "disabled" : ""%>
                    onclick="setFormAction('mnfrm', '/balance')">Balance
            </button>
            <button type="submit"
                    class="btn btn-default btn-lg active" <%= wallets == null || wallets.isEmpty() ? "disabled" : ""%>
                    onclick="setFormAction('mnfrm', '/transactions')">Transactions
            </button>
            <button type="submit"
                    class="btn btn-default btn-lg active" <%= wallets == null || wallets.isEmpty() ? "disabled" : ""%>
                    onclick="setFormAction('mnfrm', '/sendMoney')">Send money
            </button>
        </div>

        <div class="addresses_box">
            <label for="addressId">Address</label>
            <select id="addressId" name="id" class="form-control">
                <c:forEach var="wallet" items="${wallets}">
                    <option value="${wallet.id}"><c:out value="${wallet.address}"></c:out></option>
                </c:forEach>
            </select>
        </div>
    </form>
</div>

</body>
<script>

    $("#genAddress").click(function () {
        $.ajax({
            type: "POST",
            url: 'generateAddress',

            success: function (data) {
                console.log("Wallet address generation success!")
            },

            failure: function (errMsg) {
                console.log(errMsg.toString())
            }
        });
    });

    setInterval(function () {

        if ('<%= Objects.nonNull(wallets)%>') {
            getWalletsNumberAndRefreshPageIfNotEqual('<%=wallets.size() %>');
        }
    }, 5000);
</script>
</html>

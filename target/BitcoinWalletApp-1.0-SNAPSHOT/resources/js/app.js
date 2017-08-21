function setFormAction(formId, action) {
    $('#' + formId).attr('action', action);
}

function getWalletsNumberAndRefreshPageIfNotEqual(currentValue) {
    $.get("/rest/walletsNumber", function(data){
        if (data != currentValue) {
            window.location.reload(1);
        }
    });
}

function getBalanceAndRefreshPageIfNotEqual(walletId, currentValue) {
    $.get("/rest/walletBalance", {id: walletId}, function(data){
        if (data != currentValue) {
            window.location.reload(1);
        }
    });
}

function getTransactionsNumberAndRefreshPageIfNotEqual(walletId, currentValue) {
    $.get("/rest/walletTransactionsNumber", {id: walletId}, function(data){
        if (data != currentValue) {
            window.location.reload(1);
        }
    });
}
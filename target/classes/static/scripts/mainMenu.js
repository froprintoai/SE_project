// Created by Toma on Oct 6
// Edited by Toma on Oct 6
document.addEventListener("DOMContentLoaded", () => {
    const startTransaction = document.getElementById("startTransaction");
    const viewProducts = document.getElementById("viewProducts");
    const createEmp = document.getElementById("createEmp");
    const salesReport = document.getElementById("salesReport");
    const cashierReport = document.getElementById("cashierReport");

    startTransaction.addEventListener("click", startTransactionClick);
    viewProducts.addEventListener("click", viewProductsClick);
    createEmp.addEventListener("click", createEmpClick);
    salesReport.addEventListener("click", salesReportClick);
    cashierReport.addEventListener("click", cashierReportClick);
});

function isTrueString(s) {
    return (s == "true");
}

function startTransactionClick(event) {
    location.assign("/transaction/");
}

function viewProductsClick(event) {
    location.assign("/productListing/");
}

function createEmpClick(event) {
	location.assign("/employeeDetail/");
}

function salesReportClick(event) {
    // has to be implemented later
    displayError("Functionality has not been implemented.");
}

function cashierReportClick(event) {
    // has to be implemented later
    displayError("Functionality has not been implemented.");
}
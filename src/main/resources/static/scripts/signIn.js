document.addEventListener("DOMContentLoaded", function(event) {
	// TODO: Anything you want to do when the page is loaded?
	// editted by Takeshi
	console.log('DOM fully loaded and parsed');
	// editted by Takeshi
});

// copied from toma's
function validateForm() {
	// Storing field values in variables
	const empID = getEmployeeId();
	const password = getPassword();

	// Condiitons
	if(empID != '' && password != '') {
		if(!(isNaN(empID))){
			return true;
		} else {
			displayError("Please proveide a valid Employee ID.");
			return false;
		}
	} else {
		displayError("Either Employee ID or Password is empty.");
		return false;
	}
}


function getEmployeeId() {
	return getEmployeeIdElement().value;
}
function getEmployeeIdElement() {
	return document.getElementById("empId");
}

function getPassword() {
	return getPasswordElement().value;
}
function getPasswordElement() {
	return document.getElementById("password");
}
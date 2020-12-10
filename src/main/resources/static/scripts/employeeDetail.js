let hideEmployeeSavedAlertTimer = undefined;

document.addEventListener("DOMContentLoaded", () => {
	// TODO: Things that need doing when the view is loaded
	document.getElementById("saveBtn").addEventListener("click", saveActionClick);
});

// Save
function saveActionClick(event) {
	// TODO: Actually save the employee via an AJAX call
	errorMessage = validateEmpInfo()
	if (errorMessage == "") {
		classificationCode = document.getElementById("employeeType").value;
		if (classificationCode == null) {
			classificationCode = -1;
		}
		console.log("here 4");
		employeeId = document.getElementById("employeeEmployeeId").value;

		const employeeObject = {
			isActive : true,
			id : document.getElementById("employeeId").value,
			employeeId : employeeId,
			classification : classificationCode,
			// isInitialEmployee
			// managerId = 
			lastName : document.getElementById("lastName").value,
			firstName : document.getElementById("firstName").value,
			password : document.getElementById("password").value,
		};
		if (employeeId == "") {
			ajaxPost("/api/employee/", employeeObject, (callbackResponse) => {
				if (isSuccessResponse(callbackResponse)) {
					document.getElementById("toggleHidden").classList.remove("hidden");
					console.log("here 0");
					console.log(callbackResponse.data);
					if (callbackResponse.data.isInitialEmployee) {
						console.log("here 1");
						//ajaxGet(callbackResponse.data.redirectUrl, null);
						window.location.href = callbackResponse.data.redirectUrl;
					} else {
						document.getElementById("employeeEmployeeId").value = callbackResponse.data.employeeId;
						console.log(callbackResponse.data.id);
						document.getElementById("employeeId").value = callbackResponse.data.id;

					}
				}
			});
		} else {
			ajaxPatch("/api/employee/" + employeeObject.id, employeeObject, (callbackResponse) => {
				if (isSuccessResponse(callbackResponse)) {
					document.getElementById("toggleHidden").classList.remove("hidden");
					document.getElementById("employeeEmployeeId").value = callbackResponse.data.employeeId;
				}
			});
		}
	} else {
		document.getElementById("errorValidation").innerHTML = errorMessage;
		return;
	}
	displayEmployeeSavedAlertModal();
}

function validateEmpInfo() {
	var invalidElem = "";
	firstName = document.getElementById("firstName");
	lastName = document.getElementById("lastName");
	password = document.getElementById("password");
	confirm = document.getElementById("verifyPassword");
	empType = document.getElementById("employeeType");
	if (firstName.value == "") {
		invalidElem += "First Name / ";
		firstName.focus();
	}
	if (lastName.value == "") {
		invalidElem += "Last Name /";
		lastName.focus();
	}
	if (password.value == "") {
		invalidElem += "Password /";
		password.focus()
	}
	if (empType.value == "") {
		invalidElem += "Element Type /";
		empType.focus();
	}
	if (invalidElem != "") {
		invalidElem = "Following element(s) is(are) blank : " + invalidElem + " ";
	}
	if (password.value != confirm.value) {
		invalidElem += "Password and Confirm Password don't match."
		confirm.focus();
	}
	return invalidElem;
}

function displayEmployeeSavedAlertModal() {
	if (hideEmployeeSavedAlertTimer) {
		clearTimeout(hideEmployeeSavedAlertTimer);
	}

	const savedAlertModalElement = getSavedAlertModalElement();
	savedAlertModalElement.style.display = "none";
	savedAlertModalElement.style.display = "block";

	hideEmployeeSavedAlertTimer = setTimeout(hideEmployeeSavedAlertModal, 1200);
}

function hideEmployeeSavedAlertModal() {
	if (hideEmployeeSavedAlertTimer) {
		clearTimeout(hideEmployeeSavedAlertTimer);
	}

	getSavedAlertModalElement().style.display = "none";
}
// End save

//Getters and setters
function getSavedAlertModalElement() {
	return document.getElementById("employeeSavedAlertModal");
}
//End getters and setters

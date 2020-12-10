// editted by Takeshi Yamada
package edu.uark.registerapp.models.api;

public class EmployeeSignIn extends ApiResponse {
    private String employeeId;
    private String password;

	public EmployeeSignIn(final String empId, final String pass) {
        super();
        this.employeeId = empId;
        this.password = pass;
    }

    public String getEmployeeId() {
    	return this.employeeId;
    }

    public String getPassword() {
        return this.password;
    }

	public EmployeeSignIn setEmployeeId(final String empId) {
		this.employeeId = empId;
		return this;
    }

	public EmployeeSignIn setPassword(final String pass) {
		this.password = pass;
		return this;
	}
}

// editted by Takeshi
package edu.uark.registerapp.commands.employees;

import java.util.Optional;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.exceptions.UnauthorizedException;
import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.commands.activeUsers.ActiveUserDeleteCommand;
import edu.uark.registerapp.commands.employees.helpers.EmployeeHelper;

@Service
public class EmployeeSignInCommand implements VoidCommandInterface {
	@Transactional
	@Override
	public void execute() {
        EmployeeEntity employee = this.authEmployee();
        Optional<ActiveUserEntity> activeUser = 
                this.activeUserRepository.findByEmployeeId(employee.getId());
        if (activeUser.isPresent()) {
            activeUser.get().setSessionKey(this.sessionKey);
            activeUserRepository.save(activeUser.get());
        } else {
            // first delete all activeUser (actually one or zero)
            this.activeUserRepository.deleteAll();
            // then add new activeUser
            ActiveUserEntity aue = new ActiveUserEntity();
            aue.setSessionKey(this.sessionKey);
            aue.setClassification(employee.getClassification());
            aue.setEmployeeId(employee.getId());
            aue.setName(employee.getFirstName() + " " + employee.getLastName());
            activeUserRepository.save(aue);
        }
        /*
         catch (UnprocessableEntityException e) {
            return false;
        } catch (NotFoundException e) {
            return false;
        }
        */
	}
    // Helper methods
    // EmployeeId should be numeric and not be blank
    // Password should not be blank
	private void validateEmployeeInfo() {
        String empId = this.employeeSignIn.getEmployeeId();
        String password = this.employeeSignIn.getPassword();

        if (!(StringUtils.isNumeric(empId)) || password == "") { //isNumeric returns True if empId is numeric and not blank
            throw new UnprocessableEntityException("EmployeeSignIn");
        }
    }

    private EmployeeEntity authEmployee() {
        this.validateEmployeeInfo();
        String empId = this.employeeSignIn.getEmployeeId();
        String password = this.employeeSignIn.getPassword();
        Optional<EmployeeEntity> employee = 
            this.employeeRepository.findByEmployeeId(Integer.parseInt(empId));
        if (employee.isPresent()) { // if an employee with empId exists in DB
            if (!(Arrays.equals(employee.get().getPassword(),
                    EmployeeHelper.hashPassword(password)))) {
                throw new UnauthorizedException();
            }
        } else {
            throw new NotFoundException("Employee");
        }
        return employee.get();
    }
    

    // Properties
    private EmployeeSignIn employeeSignIn;
    private String sessionKey;
	public EmployeeSignInCommand setEmployeeSignIn(final EmployeeSignIn esi) {
		this.employeeSignIn= esi;
		return this;
	}
	public EmployeeSignInCommand setSessionKey(final String key) {
		this.sessionKey = key;
		return this;
	}

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
    private ActiveUserRepository activeUserRepository;
    @Autowired
    private ActiveUserDeleteCommand activeUserDeleteCommand;
}
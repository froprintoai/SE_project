package edu.uark.registerapp.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.employees.EmployeeQuery;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import edu.uark.registerapp.models.enums.EmployeeClassification;

@Controller
@RequestMapping(value = "/employeeDetail")
public class EmployeeDetailRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView start(
		@RequestParam final Map<String, String> queryParameters,
		final HttpServletRequest request
	) {
		// TODO: Logic to determine if the user associated with the current session
		//  is able to create an employee
		String sessionId = request.getSession().getId();
		Optional<ActiveUserEntity> currentUser = this.activeUserRepository.findBySessionKey(sessionId);

		if (this.employeeRepository.count() == 0) { // if no employee exists
			// serve up the EmployeeDetail view/doc
			return new ModelAndView(
				ViewNames.EMPLOYEE_DETAIL.getViewName()).addObject(
					ViewModelNames.EMPLOYEE.getValue(), (new Employee()));
		} else if (!currentUser.isPresent()) {// if there is not an active user for the current session
			// redirect to SignIn page with appropriate error messages
			return new ModelAndView(ViewNames.SIGN_IN.getViewName()).addObject(
									ViewModelNames.ERROR_MESSAGE.getValue(), "You aren't an active user, sign in first.")	;
		} else if (EmployeeClassification.isElevatedUser(currentUser.get().getClassification())) { // if current user is a manager
			// serve up the EmployeeDetail view/doc
			return new ModelAndView(
				ViewNames.EMPLOYEE_DETAIL.getViewName()).addObject(
					ViewModelNames.EMPLOYEE.getValue(), (new Employee()));
		} else {
			// reidirect to MainMenu with an appropriate error message
			return new ModelAndView(ViewNames.MAIN_MENU.getViewName()).addObject(
									ViewModelNames.ERROR_MESSAGE.getValue(), "You aren't privileged to see this page.")	;

		}

		//return new ModelAndView(ViewModelNames.EMPLOYEE_TYPES.getValue());
	}


	@RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
	public ModelAndView startWithEmployee(
		@PathVariable final UUID employeeId,
		@RequestParam final Map<String, String> queryParameters,
		final HttpServletRequest request
	) {
		System.out.println("hello from startWithEmployee");
		final Optional<ActiveUserEntity> activeUserEntity =
			this.getCurrentUser(request);

		if (!activeUserEntity.isPresent()) {
			//return this.buildInvalidSessionResponse();
			return new ModelAndView(ViewNames.SIGN_IN.getViewName()).addObject(
									ViewModelNames.ERROR_MESSAGE.getValue(), "You aren't an active user, sign in first.")	;
		} else if (!this.isElevatedUser(activeUserEntity.get())) {
			return this.buildNoPermissionsResponse();
		}

		Employee employee = new Employee();
		try {
			employee = this.employeeQuery.setRecordId(employeeId).execute();
			// include the specified employee info
			return (new ModelAndView(ViewNames.EMPLOYEE_DETAIL.getViewName())).addObject(
					ViewModelNames.EMPLOYEE.getValue(), employee);
		} catch (NotFoundException e) {
			// 
			System.out.println(e.toString());
		}
		return (new ModelAndView(ViewNames.EMPLOYEE_DETAIL.getViewName())).addObject(
				ViewModelNames.EMPLOYEE.getValue(), employee);

		// TODO: Query the employee details using the request route parameter
		// TODO: Serve up the page
		// return new ModelAndView(ViewModelNames.EMPLOYEE_TYPES.getValue());
	}

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private ActiveUserRepository activeUserRepository;
	@Autowired
	private EmployeeQuery employeeQuery;
}

// editted by Takeshi
package edu.uark.registerapp.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.commands.employees.ActiveEmployeeExistQuery;
import edu.uark.registerapp.commands.employees.EmployeeSignInCommand;
import edu.uark.registerapp.commands.exceptions.NotFoundException;

import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class SignInRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showSignInPage(@RequestParam Map<String, String> allParams) {
		// For what allParams should be used???
		try {
			// if employees exist
			if (this.activeEmployeeExistQuery.execute()) { 
				if (allParams.containsKey("employeeId")) {
					return new ModelAndView(ViewNames.SIGN_IN.getViewName()).addObject("employeeId", allParams.get("employeeId"));
				}
				return new ModelAndView(ViewNames.SIGN_IN.getViewName());
			}	
		} catch (NotFoundException e) { // no employee exists
			String redirectUrl = "redirect:/" + ViewNames.EMPLOYEE_DETAIL.getViewName();
			return new ModelAndView(redirectUrl);
		}
		return (new ModelAndView(ViewNames.SIGN_IN.getViewName()))
			.addObject(ViewModelNames.ERROR_MESSAGE.getValue(), "should not be reached here");
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView performSignIn(
		EmployeeSignIn empInfo, 
		HttpServletRequest request
	) {
		String sessionID = request.getSession().getId();
		try {
			this.employeeSignInCommand.setEmployeeSignIn(empInfo)
				.setSessionKey(sessionID).execute();
			return new ModelAndView(
				REDIRECT_PREPEND.concat(
					ViewNames.MAIN_MENU.getRoute()));
		} catch (Exception e) {
			return (new ModelAndView(ViewNames.SIGN_IN.getViewName())
				.addObject(
					ViewModelNames.ERROR_MESSAGE.getValue(), e.getMessage()));
		}

		// TODO: Use the credentials provided in the request body
		//  and the "id" property of the (HttpServletRequest)request.getSession() variable
		//  to sign in the user

	}

	// Properties
	@Autowired
	private ActiveEmployeeExistQuery activeEmployeeExistQuery;
	@Autowired
	private EmployeeSignInCommand employeeSignInCommand;
}

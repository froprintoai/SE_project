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
import edu.uark.registerapp.models.api.Cart;
import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.commands.carts.CartByEmployeeIdQuery;
import edu.uark.registerapp.commands.carts.CartDeleteCommand;
import edu.uark.registerapp.commands.employees.ActiveEmployeeExistQuery;
import edu.uark.registerapp.commands.employees.EmployeeSignInCommand;
import edu.uark.registerapp.commands.exceptions.NotFoundException;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/cancelTransaction")
public class CancelTransactionRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView cancelTransaction(final HttpServletRequest request) {

		final Optional<ActiveUserEntity> activeUserEntity =
			this.getCurrentUser(request);
		if (!activeUserEntity.isPresent()) {
			return this.buildInvalidSessionResponse();
        }
        // empty the shopping cart
        Cart cart = this.cartByEmployeeIdQuery.
            setEmployeeId(activeUserEntity.get().getEmployeeId()).execute();
        this.cartDeleteCommand.setCartId(cart.getId()).execute();

        return new ModelAndView(
            REDIRECT_PREPEND.concat(
                ViewNames.MAIN_MENU.getRoute()));
	}


	// Properties
    @Autowired
    private CartDeleteCommand cartDeleteCommand;
    @Autowired
    private CartByEmployeeIdQuery cartByEmployeeIdQuery;
}

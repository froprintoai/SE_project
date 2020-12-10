package edu.uark.registerapp.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.Cart;
import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.models.api.Item;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.commands.employees.ActiveEmployeeExistQuery;
import edu.uark.registerapp.commands.employees.EmployeeSignInCommand;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.carts.CartByEmployeeIdQuery;
import edu.uark.registerapp.commands.carts.CartUpdateCommand;
import edu.uark.registerapp.commands.carts.ItemsByCartCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(value = "/transaction")
public class TransactionRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showTransactionPage(final HttpServletRequest request) {
        // check if the current user is active
		final Optional<ActiveUserEntity> activeUserEntity =
			this.getCurrentUser(request);
		if (!activeUserEntity.isPresent()) {
			return this.buildInvalidSessionResponse();
        }

        ModelAndView modelAndView = new ModelAndView(ViewNames.TRANSACTION.getViewName());

        // get cart by activeUser's employeeId
        try {
            Cart cart = this.cartByEmployeeIdQuery.
                setEmployeeId(activeUserEntity.get().getEmployeeId()).execute();
            // if all the quantities are zeros, 
            Integer totalQuantity = 0;
            List<Integer> productCounts = cart.getProductCounts();
            for (Integer quantity : productCounts) {
                totalQuantity += quantity;
            }
            if (totalQuantity == 0) {
                return modelAndView.addObject("isCartEmpty", true).addObject("searchText", "");
            } else {
                List<Item> items = this.itemsByCartCommand.setCart(cart).execute();
                int total = 0;
                for (Item item : items) {
                    total += item.getSubtotal();
                }
                return modelAndView.addObject("isCartEmpty", false).addObject("searchText", "").addObject("items", items).addObject("total", total);
            }

        } catch (NotFoundException e) {
            // there is no cart for the current user
            return modelAndView.addObject("isCartEmpty", true).addObject("searchText", "");
        }

	}
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView applyChanges(
        @RequestParam List<Integer> quantity,
        @RequestParam String[] productId,
        final HttpServletRequest request
    ) {
        // convert string array of UUID into UUID array
        List<UUID> uuids = new ArrayList<UUID>();
        for (String id : productId) {
            uuids.add(UUID.fromString(id));
        }
        // get current user
		final Optional<ActiveUserEntity> activeUserEntity =
			this.getCurrentUser(request);
		if (!activeUserEntity.isPresent()) {
			return this.buildInvalidSessionResponse();
        }

        // fetch cart object using the current user
        Cart cart = this.cartByEmployeeIdQuery.
            setEmployeeId(activeUserEntity.get().getEmployeeId()).execute();

        // change cart contents using the inputs
        cart.setProductCounts(quantity);
        cart.setProductIds(uuids);

        // update cart table using it
        this.cartUpdateCommand.setApiCart(cart).execute();

        return new ModelAndView(
            REDIRECT_PREPEND.concat(
                ViewNames.TRANSACTION.getRoute()));
    }
	// Properties
	@Autowired
    private CartByEmployeeIdQuery cartByEmployeeIdQuery;
    @Autowired
    private ItemsByCartCommand itemsByCartCommand;
    @Autowired
    private CartUpdateCommand cartUpdateCommand;
}






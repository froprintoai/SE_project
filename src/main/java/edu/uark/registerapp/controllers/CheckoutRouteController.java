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
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.commands.carts.CartByEmployeeIdQuery;
import edu.uark.registerapp.commands.carts.CartDeleteCommand;
import edu.uark.registerapp.commands.employees.ActiveEmployeeExistQuery;
import edu.uark.registerapp.commands.employees.EmployeeSignInCommand;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.products.ProductQuery;
import edu.uark.registerapp.commands.products.ProductUpdateCommand;
import edu.uark.registerapp.commands.products.ProductsQuery;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/checkout")
public class CheckoutRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView checkout(
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

        // update product table
        for (int i = 0; i < uuids.size(); i++) {
            Product product = this.productQuery.setProductId(uuids.get(i)).execute();
            product.setCount(product.getCount() - quantity.get(i));
            this.productUpdateCommand.setProductId(uuids.get(i)).setApiProduct(product).execute();
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
    private CartByEmployeeIdQuery cartByEmployeeIdQuery;
    @Autowired
    private ProductQuery productQuery;
    @Autowired
    private ProductUpdateCommand productUpdateCommand;
    @Autowired
    private CartDeleteCommand cartDeleteCommand;
}


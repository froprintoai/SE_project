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
import edu.uark.registerapp.commands.carts.CartCreateCommand;
import edu.uark.registerapp.commands.carts.CartUpdateCommand;
import edu.uark.registerapp.commands.employees.ActiveEmployeeExistQuery;
import edu.uark.registerapp.commands.employees.EmployeeSignInCommand;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.products.ProductsQuery;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/addToCart")
public class AddToCartRouteController extends BaseRouteController {
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView addToCart(
        @RequestParam List<Integer> quantity,
        @RequestParam String[] productId,
        final HttpServletRequest request
    ) {
        // convert string array of UUID into UUID array
        List<UUID> uuids = new ArrayList<UUID>();
        for (String id : productId) {
            uuids.add(UUID.fromString(id));
        }

        if (uuids.size() == 0) {
            return new ModelAndView(
                REDIRECT_PREPEND.concat(
                    ViewNames.TRANSACTION.getRoute()));
        }

		final Optional<ActiveUserEntity> activeUserEntity =
			this.getCurrentUser(request);
		if (!activeUserEntity.isPresent()) {
			return this.buildInvalidSessionResponse();
        }

        try {
            System.out.println("######here1#####");
            Cart cart = this.cartByEmployeeIdQuery.
                setEmployeeId(activeUserEntity.get().getEmployeeId()).execute();
            for (int i = 0; i < uuids.size(); i++) {
                if (quantity.get(i) != 0) {
                    int idx = cart.getProductIds().indexOf(uuids.get(i));
                    if (idx == -1) { // Not Found
                        cart.getProductIds().add(uuids.get(i));
                        cart.getProductCounts().add(quantity.get(i));
                    } else { // Found 
                        cart.getProductCounts().set(idx, cart.getProductCounts().get(idx) + quantity.get(i));
                    }
                }
            }
            this.cartUpdateCommand.setApiCart(cart).execute();
            //cart update
        } catch (NotFoundException e) {
            System.out.println("######here2#####");
            //cart create
            Cart cart = new Cart();
            cart.setEmployeeId(activeUserEntity.get().getEmployeeId());
            List<UUID> productIds = new ArrayList<UUID>();
            List<Integer> productCounts = new ArrayList<Integer>();
            for (int i = 0; i < uuids.size(); i++) {
                if (quantity.get(i) != 0) {
                    productIds.add(uuids.get(i));
                    productCounts.add(quantity.get(i));
                }
            }
            cart.setProductCounts(productCounts);
            cart.setProductIds(productIds);
            
            System.out.println("######here3#####");
            try {
                this.cartCreateCommand.setApiCart(cart).execute();
            } catch (Exception e0) {
                System.out.println(e0.toString());
                System.out.println("creation of cart failed");
            }
            System.out.println("######here4#####");
        }

        return new ModelAndView(
            REDIRECT_PREPEND.concat(
                ViewNames.TRANSACTION.getRoute()));
    }

	// Properties
	@Autowired
    private CartByEmployeeIdQuery cartByEmployeeIdQuery;
    @Autowired
    private CartCreateCommand cartCreateCommand;
    @Autowired
    private CartUpdateCommand cartUpdateCommand;
}



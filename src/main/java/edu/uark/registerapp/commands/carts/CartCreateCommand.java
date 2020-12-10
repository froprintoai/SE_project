package edu.uark.registerapp.commands.carts;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.employees.EmployeeQuery;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Cart;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.CartEntity;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.enums.EmployeeClassification;
import edu.uark.registerapp.models.repositories.CartRepository;
import edu.uark.registerapp.models.repositories.EmployeeRepository;

@Service
public class CartCreateCommand implements ResultCommandInterface<Cart> {
	@Override
	public Cart execute() {
        System.out.println("h0");
        this.validateCart();
        System.out.println("h1");
        System.out.println(this.apiCart.getEmployeeId());
        for (int i = 0; i < this.apiCart.getProductCounts().size(); i++) {
            System.out.println(this.apiCart.getProductCounts().get(i));
            System.out.println(this.apiCart.getProductIds().get(i));
        }
        CartEntity cart = this.cartRepository.save(
            new CartEntity(this.apiCart));
        System.out.println("h2");
        this.apiCart = new Cart(cart); // because it need UUID created by postgres
        System.out.println("h3");
        return this.apiCart;
	}

	// Helper methods
	private void validateCart() {
        // check if there exists an employee with the apiCart.employeeId 
        this.employeeQuery.setRecordId(this.apiCart.getEmployeeId()).execute();
        // check if product_ids_array has the same number of elements as product_count_array
        if (this.apiCart.getProductCounts().size() != this.apiCart.getProductIds().size()) {
			throw new UnprocessableEntityException("cart");
        }

	}

	// Properties
    private Cart apiCart;
	public Cart getApiCart() {
		return this.apiCart;
	}
	public CartCreateCommand setApiCart(final Cart apiCart) {
		this.apiCart = apiCart;
		return this;
    }

	@Autowired
    private CartRepository cartRepository;
    @Autowired
    private EmployeeQuery employeeQuery;
}


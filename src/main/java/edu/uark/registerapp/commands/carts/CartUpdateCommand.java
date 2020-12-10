package edu.uark.registerapp.commands.carts;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.employees.EmployeeQuery;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Cart;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.CartEntity;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.repositories.CartRepository;
import edu.uark.registerapp.models.repositories.EmployeeRepository;

@Service
public class CartUpdateCommand implements ResultCommandInterface<Cart> {
	@Transactional
	@Override
	public Cart execute() {
        this.validateCart();
        final Optional<CartEntity> cartEntity = 
            this.cartRepository.findByEmployeeId(this.apiCart.getEmployeeId());
        if (!cartEntity.isPresent()) {
            throw new NotFoundException("Cart");
        }

        this.apiCart = cartEntity.get().synchronize(this.apiCart);

		// Write, via an UPDATE, any changes to the database.
		this.cartRepository.save(cartEntity.get());

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
	public CartUpdateCommand setApiCart(final Cart apiCart) {
		this.apiCart = apiCart;
		return this;
	}
	
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private EmployeeQuery employeeQuery;
}


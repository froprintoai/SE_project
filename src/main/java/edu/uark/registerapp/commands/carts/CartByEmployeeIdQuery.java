package edu.uark.registerapp.commands.carts;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.api.Cart;
import edu.uark.registerapp.models.entities.CartEntity;
import edu.uark.registerapp.models.repositories.CartRepository;

@Service
public class CartByEmployeeIdQuery implements ResultCommandInterface<Cart> {
	@Override
	public Cart execute() {
		final Optional<CartEntity> cartEntity =
			this.cartRepository.findByEmployeeId(this.employeeId);
		if (cartEntity.isPresent()) {
			return new Cart(cartEntity.get());
		} else {
			throw new NotFoundException("Cart");
		}
	}

	// Properties
	private UUID employeeId;
	public UUID getEmployeeId() {
		return this.employeeId;
	}
	public CartByEmployeeIdQuery setEmployeeId(final UUID employeeId) {
		this.employeeId = employeeId;
		return this;
	}

	@Autowired
	private CartRepository cartRepository;
}


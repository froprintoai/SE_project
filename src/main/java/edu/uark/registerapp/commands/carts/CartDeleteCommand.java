package edu.uark.registerapp.commands.carts;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.entities.CartEntity;
import edu.uark.registerapp.models.entities.ProductEntity;
import edu.uark.registerapp.models.repositories.CartRepository;
import edu.uark.registerapp.models.repositories.ProductRepository;

@Service
public class CartDeleteCommand implements VoidCommandInterface {
	@Transactional
	@Override
	public void execute() {
		final Optional<CartEntity> cartEntity =
			this.cartRepository.findById(this.cartId);
		if (!cartEntity.isPresent()) { // No record with the associated record ID exists in the database.
			throw new NotFoundException("Product");
		}

		this.cartRepository.delete(cartEntity.get());
	}

	// Properties
	private UUID cartId;
	public UUID getCartId() {
		return this.cartId;
	}
	public CartDeleteCommand setCartId(final UUID cartId) {
		this.cartId = cartId;
		return this;
	}
	
	@Autowired
	private CartRepository cartRepository;
}


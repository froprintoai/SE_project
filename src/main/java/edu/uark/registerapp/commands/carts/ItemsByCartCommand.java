package edu.uark.registerapp.commands.carts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.api.Cart;
import edu.uark.registerapp.models.api.Item;
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.models.entities.CartEntity;
import edu.uark.registerapp.models.entities.ProductEntity;
import edu.uark.registerapp.models.repositories.CartRepository;
import edu.uark.registerapp.models.repositories.ProductRepository;

@Service
public class ItemsByCartCommand implements ResultCommandInterface<List<Item>> {
    @Override
	public List<Item> execute() {
        List<Item> items = new ArrayList<Item>();
        List<UUID> productIds = this.cart.getProductIds();
        for (int i = 0; i < productIds.size(); i++) {
            Optional<ProductEntity> productEntity = this.productRepository.findById(productIds.get(i));
            if (productEntity.isPresent()) {
                Product product = new Product(productEntity.get());
                Item item = new Item(product, this.cart.getProductCounts().get(i));
                items.add(item);
            } else {
                throw new NotFoundException("product");
            }
        }
        return items;

	}

	// Properties
	private Cart cart;
	public Cart getCart() {
		return this.cart;
	}
	public ItemsByCartCommand setCart(final Cart cart) {
		this.cart = cart;
		return this;
	}

	@Autowired
	private ProductRepository productRepository;
}



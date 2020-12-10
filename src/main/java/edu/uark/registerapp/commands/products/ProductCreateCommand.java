package edu.uark.registerapp.commands.products;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.ConflictException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.models.entities.ProductEntity;
import edu.uark.registerapp.models.repositories.ProductRepository;

@Service
public class ProductCreateCommand implements ResultCommandInterface<Product> {
	@Override
	public Product execute() {
		this.validateProperties();

		final ProductEntity createdProductEntity = this.createProductEntity();

		// Synchronize information generated by the database upon INSERT.
		this.apiProduct.setId(createdProductEntity.getId()); 
		this.apiProduct.setCreatedOn(createdProductEntity.getCreatedOn());

		return this.apiProduct;
	}

	// Helper methods
	private void validateProperties() {
		if (StringUtils.isBlank(this.apiProduct.getLookupCode())) {
			throw new UnprocessableEntityException("lookupcode");
		}
	}

	@Transactional
	private ProductEntity createProductEntity() {
		final Optional<ProductEntity> queriedProductEntity =
			this.productRepository
				.findByLookupCode(this.apiProduct.getLookupCode());

		if (queriedProductEntity.isPresent()) {
			// Lookupcode already defined for another product.
			throw new ConflictException("lookupcode");
		}

		// No ENTITY object was returned from the database, thus the API object's
		// lookupcode must be unique.

		// Write, via an INSERT, the new record to the database.
		return this.productRepository.save(
			new ProductEntity(apiProduct));
	}

	// Properties
	private Product apiProduct;
	public Product getApiProduct() {
		return this.apiProduct;
	}
	public ProductCreateCommand setApiProduct(final Product apiProduct) {
		this.apiProduct = apiProduct;
		return this;
	}

	@Autowired
	private ProductRepository productRepository;
}

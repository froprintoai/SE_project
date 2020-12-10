package edu.uark.registerapp.models.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.uark.registerapp.commands.employees.helpers.EmployeeHelper;
import edu.uark.registerapp.models.entities.CartEntity;
import edu.uark.registerapp.models.entities.EmployeeEntity;

public class Item extends ApiResponse {
    private Product product;
    public Product getProduct() {
        return this.product;
    }
    public Item setProduct(final Product product) {
        this.product = product;
        return this;
    }

	private int quantity;
	public int getQuantity() {
		return this.quantity;
	}
	public Item setQuantity(final int quantity) {
		this.quantity = quantity;
		return this;
    }

    private int subtotal;
	public int getSubtotal() {
		return this.subtotal;
	}
	public Item setSubtotal(final int subtotal) {
		this.subtotal = subtotal;
		return this;
    }


	public Item() {
		super();
        this.product = new Product();
        this.quantity = 0;
        this.subtotal = 0;
    }

	public Item(final Product product, final int quantity) {
		super();
        this.product = product;
        this.quantity = quantity;
        this.subtotal = this.product.getPrice() * quantity;
	}

}


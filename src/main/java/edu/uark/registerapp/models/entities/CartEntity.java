package edu.uark.registerapp.models.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import edu.uark.registerapp.commands.employees.helpers.EmployeeHelper;
import edu.uark.registerapp.models.api.Cart;
import edu.uark.registerapp.models.api.Employee;

@Entity
@Table(name="cart")
public class CartEntity {
    @Id
    @Column(name="id", updatable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private final UUID id;

	public UUID getId() {
		return this.id;
	}

	@Column(name = "employeeid")
	private UUID employeeId;

	public UUID getEmployeeId() {
		return this.employeeId;
	}

	public CartEntity setEmployeeId(final UUID employeeId) {
		this.employeeId = employeeId;
		return this;
	}

    @Column(name = "productids")
    @ElementCollection(targetClass = UUID.class)
	private List<UUID> productIds;

	public List<UUID> getProductIds() {
		return this.productIds;
	}

	public CartEntity setProductIds(final List<UUID> productIds) {
		this.productIds = productIds;
		return this;
	}

	@Column(name = "productcounts")
    @ElementCollection(targetClass = Integer.class)
	private List<Integer> productCounts;

	public List<Integer> getProductCounts() {
		return this.productCounts;
	}

	public CartEntity setProductCounts(final List<Integer> productCounts) {
		this.productCounts = productCounts;
		return this;
    }

	public Cart synchronize(final Cart apiCart) {
        this.setEmployeeId(apiCart.getEmployeeId());
        this.setProductIds(apiCart.getProductIds());
        this.setProductCounts(apiCart.getProductCounts());
        apiCart.setId(this.getId());

		return apiCart;
	}

    public CartEntity() {
		this.id = new UUID(0, 0);
		this.employeeId = new UUID(0, 0);
        this.productIds = new ArrayList<UUID>();
        this.productCounts = new ArrayList<Integer>();
    }

	public CartEntity(final Cart apiCart) {
        this.id = new UUID(0, 0);
        this.employeeId = apiCart.getEmployeeId();
        this.productIds = apiCart.getProductIds();
        this.productCounts = apiCart.getProductCounts();
	}
}
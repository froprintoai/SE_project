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

public class Cart extends ApiResponse {
    private UUID id;
    public UUID getId() {
    	return this.id;
    }
    public Cart setId(final UUID id) {
		this.id = id;
		return this;
	}

	private UUID employeeId;
	public UUID getEmployeeId() {
		return this.employeeId;
	}
	public Cart setEmployeeId(final UUID employeeId) {
		this.employeeId = employeeId;
		return this;
	}

    private List<UUID> productIds;
	public List<UUID> getProductIds() {
		return this.productIds;
	}
	public Cart setProductIds(final List<UUID> productIds) {
		this.productIds = productIds;
		return this;
    }
    
    private List<Integer> productCounts;
    public List<Integer> getProductCounts() {
        return this.productCounts;
    }
	public Cart setProductCounts(final List<Integer> productCounts) {
		this.productCounts = productCounts;
		return this;
    }

	public Cart() {
		super();

        this.id = new UUID(0, 0);
        this.employeeId = new UUID(0, 0);
        this.productIds = new ArrayList<UUID>();
        this.productCounts = new ArrayList<Integer>();
	}

	public Cart(final CartEntity cartEntity) {
		super(false);

        this.id = cartEntity.getId();
        this.employeeId = cartEntity.getEmployeeId();
        this.productIds = cartEntity.getProductIds();
        this.productCounts = cartEntity.getProductCounts();
	}
}


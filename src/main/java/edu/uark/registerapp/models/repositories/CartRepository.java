package edu.uark.registerapp.models.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import edu.uark.registerapp.models.entities.CartEntity;

public interface CartRepository extends CrudRepository<CartEntity, UUID> {
	Optional<CartEntity> findById(UUID id);
    Optional<CartEntity> findByEmployeeId(UUID employeeId);
}

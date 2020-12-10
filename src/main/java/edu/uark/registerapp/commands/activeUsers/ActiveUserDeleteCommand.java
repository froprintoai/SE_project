// editted by Takeshi
package edu.uark.registerapp.commands.activeUsers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;

@Service
public class ActiveUserDeleteCommand implements VoidCommandInterface {
	@Transactional
    @Override
    // Does this command need to support for verifying Employee First & Last name?
	public void execute() {
		final Optional<ActiveUserEntity> activeUser =
			this.activeUserRepository.findBySessionKey(this.sessionKey);
		if (!activeUser.isPresent()) { // No record with the associated record ID exists in the database.
			throw new NotFoundException("ActiveUser");
		}

		this.activeUserRepository.delete(activeUser.get());
	}

	// Properties
	private String sessionKey;
	public ActiveUserDeleteCommand setSessionKey(final String key) {
		this.sessionKey = key;
		return this;
	}
	
	@Autowired
	private ActiveUserRepository activeUserRepository;
}

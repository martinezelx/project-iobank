package io.builders.iobank.domain.port.repository.database;

import io.builders.iobank.domain.model.ProtocolType;
import io.builders.iobank.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByIdAndProtocol(String id, ProtocolType protocol);
}
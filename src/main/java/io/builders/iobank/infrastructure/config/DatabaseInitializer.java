package io.builders.iobank.infrastructure.config;

import io.builders.iobank.domain.model.Account;
import io.builders.iobank.domain.model.ProtocolType;
import io.builders.iobank.domain.model.Transaction;
import io.builders.iobank.domain.model.User;
import io.builders.iobank.domain.port.repository.database.AccountRepository;
import io.builders.iobank.domain.port.repository.database.TransactionRepository;
import io.builders.iobank.domain.port.repository.database.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Configuration
public class DatabaseInitializer {

	@Bean
	public CommandLineRunner usersAndAccountsDataLoader(UserRepository userRepository, AccountRepository accountRepository) {

		User user1 = new User(1L, "Luis Martinez","martinezelx","lm@gmail.com", null);
		User user2 = new User(2L, "Cristinel Pavel","pcristinel","cp@gmail.com", null);
		User user3 = new User(3L, "Jaime Montesinos","jmonty","jm@gmail.com", null);
		User user4 = new User(4L, "Teodor Vladislavov", "teodosi", "tv@gmail.com", null);

		return args -> {
			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);
			userRepository.save(user4);

			accountRepository.save(new Account("ES2121", BigDecimal.valueOf(1000), ProtocolType.EUR, user1));
			accountRepository.save(new Account("0x1234", BigDecimal.valueOf(0.5), ProtocolType.ETH, user1));
			accountRepository.save(new Account("ES2222", BigDecimal.valueOf(500), ProtocolType.USD, user2));
			accountRepository.save(new Account("bc1io21bank", BigDecimal.valueOf(5), ProtocolType.BTC, user3));
			accountRepository.save(new Account("bc1io22bank", BigDecimal.valueOf(0.1), ProtocolType.BTC, user3));
			accountRepository.save(new Account("DiO22BanK", BigDecimal.valueOf(50), ProtocolType.DOGE, user4));
		};
	}

	@Bean
	public CommandLineRunner transactionsDataLoader(TransactionRepository transactionRepository) {
		return args -> {
			transactionRepository.save(
					new Transaction(UUID.randomUUID(),
							"bc1io22bank",
							"bc1io21bank",
							BigDecimal.valueOf(1),
							ProtocolType.BTC,
							Instant.now(),
							BigDecimal.valueOf(0),
							"test tx"));
		};
	}
}

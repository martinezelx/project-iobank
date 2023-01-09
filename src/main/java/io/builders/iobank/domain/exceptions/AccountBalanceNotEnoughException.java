package io.builders.iobank.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountBalanceNotEnoughException extends RuntimeException {
	public AccountBalanceNotEnoughException(String message) {
		super(message);
	}
}
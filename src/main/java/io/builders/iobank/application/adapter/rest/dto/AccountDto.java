package io.builders.iobank.application.adapter.rest.dto;

import io.builders.iobank.domain.model.ProtocolType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @NotBlank(message = "Account needs a id.")
    private String id;

    @NotNull(message = "Account needs a balance.")
    @PositiveOrZero
    private BigDecimal balance;

    @NotNull(message = "Account needs a protocol.")
    private ProtocolType protocol;

    @NotNull(message = "Account needs a user id.")
    private Long userId;
}

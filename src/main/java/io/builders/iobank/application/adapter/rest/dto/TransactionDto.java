package io.builders.iobank.application.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.builders.iobank.domain.model.ProtocolType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID reference;

    @NotBlank(message = "Transaction needs a source.")
    private String source;

    @NotBlank(message = "Transaction needs a destination.")
    private String destination;

    @NotNull(message = "Transaction needs a amount.")
    @PositiveOrZero
    private BigDecimal amount;

    @NotNull(message = "Transaction needs a protocol.")
    private ProtocolType protocol;

    @NotNull(message = "Transaction needs a date.")
    private Instant date;

    @NotNull(message = "Transaction needs a fee.")
    @PositiveOrZero
    private BigDecimal fee;

    private String description;
}

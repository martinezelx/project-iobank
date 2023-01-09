package io.builders.iobank.application.adapter.rest.dto;

import io.builders.iobank.domain.model.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "User needs a full name.")
    @Size(min = 4, max = 15)
    private String fullName;

    @NotBlank(message = "User needs a user name.")
    @Size(min = 4, max = 15)
    private String userName;

    @NotBlank(message = "User needs a email.")
    @Email
    private String email;

    private List<Account> accounts;
}
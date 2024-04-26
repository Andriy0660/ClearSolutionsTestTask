package com.example.clearsolutionstesttask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @Email(regexp = ".+@.+\\..+", message = "Invalid email format")
    @NotBlank(message = "Email can not be blank")
    private String email;

    @NotBlank(message = "First name can not be blank")
    private String firstName;

    @NotBlank(message = "Last name can not be blank")
    private String lastName;

    @NotNull(message = "Birth date can not be blank")
    private LocalDate birthDate;

    private String phone;
    private String address;

}
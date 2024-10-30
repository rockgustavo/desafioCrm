package com.rockgustavo.desafiocrm.rest.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    @NotBlank(message = "Campo nome precisa ser preenchido!")
    private String name;
    @NotBlank(message = "Campo e-mail precisa ser preenchido!")
    private String email;
    @NotBlank(message = "Campo senha precisa ser preenchido!")
    private String password;
}

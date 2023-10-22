package com.gladun.buscompany.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDtoRequest {

    @NotEmpty(message = "{login.notempty}")
    @Size(max = 50, message = "{login.size}")
    private String login;

    @NotEmpty(message = "{password.notempty}")
    @Size(min = 8, max = 50, message = "{password.size}")
    private String password;

}

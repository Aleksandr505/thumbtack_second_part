package com.gladun.buscompany.dto.request;

import com.gladun.buscompany.validation.ContactNumberConstraint;
import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterClientDtoRequest {

    @NotEmpty(message = "{surname.notempty}")
    @Size(max = 50, message = "{surname.size}")
    @Pattern(regexp = "([А-Яа-я\\-\\s])+", message = "{surname.pattern}")
    private String surname;

    @NotEmpty(message = "{name.notempty}")
    @Size(max = 50, message = "{name.size}")
    @Pattern(regexp = "([А-Яа-я\\-\\s])+", message = "{name.pattern}")
    private String name;

    @Size(max = 50, message = "{patronymic.size}")
    @Pattern(regexp = "([А-Яа-я\\-\\s])+", message = "{patronymic.pattern}")
    private String patronymic;

    @NotEmpty(message = "{login.notempty}")
    @Size(max = 50, message = "{login.size}")
    private String login;

    @NotEmpty(message = "{password.notempty}")
    @Size(min = 8, max = 50, message = "{password.size}")
    private String password;

    @NotEmpty(message = "{email.notempty}")
    @Email(message = "{email.email}")
    private String email;

    @NotEmpty(message = "{phone.notempty}")
    @ContactNumberConstraint(message = "{phone.constraints}")
    private String phone;

}

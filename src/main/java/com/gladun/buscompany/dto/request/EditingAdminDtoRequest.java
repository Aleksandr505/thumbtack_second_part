package com.gladun.buscompany.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditingAdminDtoRequest {

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

    @NotEmpty(message = "{position.notempty}")
    private String position;

    @NotEmpty(message = "{password.notempty}")
    @Size(min = 8, max = 50, message = "{password.size}")
    private String oldPassword;

    @NotEmpty(message = "{password.notempty}")
    @Size(min = 8, max = 50, message = "{password.size}")
    private String newPassword;

}

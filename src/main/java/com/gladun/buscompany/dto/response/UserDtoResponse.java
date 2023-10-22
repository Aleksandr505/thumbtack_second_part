package com.gladun.buscompany.dto.response;

import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserDtoResponse {

    private int id;
    private String surname;
    private String name;
    private String patronymic;

}

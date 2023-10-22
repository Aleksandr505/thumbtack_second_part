package com.gladun.buscompany.dto.response;

import com.gladun.buscompany.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditingClientDtoResponse {

    private String surname;
    private String name;
    private String patronymic;
    private String email;
    private String phone;
    private final RoleEnum userType = RoleEnum.CLIENT;

}

package com.gladun.buscompany.dto.response;

import com.gladun.buscompany.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditingAdminDtoResponse {

    private String surname;
    private String name;
    private String patronymic;
    private String position;
    private final RoleEnum userType = RoleEnum.ADMIN;

}

package com.gladun.buscompany.dto.response;

import com.gladun.buscompany.model.RoleEnum;
import lombok.*;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminDtoResponse extends UserDtoResponse {

    private String position;
    private final RoleEnum userType = RoleEnum.ADMIN;

}

package com.gladun.buscompany.dto.response;

import com.gladun.buscompany.model.RoleEnum;
import lombok.*;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ClientDtoResponse extends UserDtoResponse {

    private String email;
    private String phone;
    private final RoleEnum userType = RoleEnum.CLIENT;

}

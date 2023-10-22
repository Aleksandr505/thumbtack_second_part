package com.gladun.buscompany.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {

    @NotEmpty(message = "{passenger.firstName.notempty}")
    private String firstName;

    @NotEmpty(message = "{passenger.lastName.notempty}")
    private String lastName;

    @NotEmpty(message = "{passenger.passport.notempty}")
    private String passport;

}

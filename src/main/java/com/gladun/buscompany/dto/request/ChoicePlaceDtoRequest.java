package com.gladun.buscompany.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoicePlaceDtoRequest {

    @Min(value = 1, message = "{orderId.min}")
    private int orderId;

    @NotEmpty(message = "{passenger.lastName.notempty}")
    private String lastName;

    @NotEmpty(message = "{passenger.firstName.notempty}")
    private String firstName;

    @NotEmpty(message = "{passenger.passport.notempty}")
    private String passport;

    @Min(value = 1, message = "{place.min}")
    private int place;

}

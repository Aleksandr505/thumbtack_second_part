package com.gladun.buscompany.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoicePlaceDtoResponse {

    private int orderId;

    private String ticket;

    private String lastName;

    private String firstName;

    private String passport;

    private int place;

}

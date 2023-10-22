package com.gladun.buscompany.dto.request;

import com.gladun.buscompany.dto.PassengerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDtoRequest {

    @Min(value = 1, message = "{tripId.min}")
    private int tripId;

    @NotEmpty(message = "{date.notempty}")
    private String date;

    @Valid
    @NotNull(message = "{passengers.notempty}")
    private List<PassengerDto> passengers;

}

package com.gladun.buscompany.dto.response;

import com.gladun.buscompany.dto.PassengerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDtoResponse {

    private int orderId;

    private int tripId;

    private String fromStation;

    private String toStation;

    private String busName;

    private String date;

    private LocalTime start;

    private long duration;

    private String price;

    private String totalPrice;

    List<PassengerDto> passengers;

}

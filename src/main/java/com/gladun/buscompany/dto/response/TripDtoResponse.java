package com.gladun.buscompany.dto.response;

import com.gladun.buscompany.dto.request.TripDtoRequest;
import com.gladun.buscompany.model.Bus;
import com.gladun.buscompany.model.TripDate;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDtoResponse {

    private int tripId;

    private String busName;

    private int placeCount;

    private String fromStation;

    private String toStation;

    private LocalTime start;

    private long duration;

    private String price;

    private boolean approved;

    private TripDtoRequest.ScheduleDto scheduleDto;

    private String[] dates;

}

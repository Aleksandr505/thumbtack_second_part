package com.gladun.buscompany.dto.request;

import com.gladun.buscompany.validation.ScheduleCheck;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ScheduleCheck(
        schedule = "scheduleDto",
        dates = "dates"
)
public class TripDtoRequest {

    @NotEmpty(message = "{busName.notempty}")
    private String busName;

    @NotEmpty(message = "{fromStation.notempty}")
    private String fromStation;

    @NotEmpty(message = "{toStation.notempty}")
    private String toStation;

    @NotNull(message = "{start.notnull}")
    @Pattern(regexp = "^(?:0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", message = "{start.pattern}")
    private String start;

    @Min(value = 0, message = "{duration.notnegative}")
    private long duration;

    @NotEmpty(message = "{price.notempty}")
    @Pattern(regexp = "(\\d+)\\s+руб\\.", message = "{price.pattern}")
    private String price;

    @Valid
    private ScheduleDto scheduleDto;

    private String[] dates;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleDto {

        @NotEmpty(message = "{schedule.fromDate.notempty}")
        @Pattern(regexp = "^\\d{4}-((0\\d)|(1[012]))-(([012]\\d)|3[01])$", message = "{schedule.fromDate.pattern}")
        private String fromDate;

        @NotEmpty(message = "{schedule.toDate.notempty}")
        @Pattern(regexp = "^\\d{4}-((0\\d)|(1[012]))-(([012]\\d)|3[01])$", message = "{schedule.toDate.pattern}")
        private String toDate;

        @NotEmpty(message = "{schedule.period.notempty}")
        private String period;

    }

}

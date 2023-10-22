package com.gladun.buscompany.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Trip {

    private int id;
    private Bus bus;
    private String fromStation;
    private String toStation;
    private LocalTime start;
    private long duration;
    private String price;
    private boolean approved;
    private List<TripDate> tripDates;

    public Trip(int id, Bus bus, String fromStation, String toStation, LocalTime start, long duration, String price, boolean approved, List<TripDate> tripDates) {
        this(bus, fromStation, toStation, start, duration, price, approved, tripDates);
        this.id = id;
    }

    public Trip(Bus bus, String fromStation, String toStation, LocalTime start, long duration, String price, boolean approved, List<TripDate> tripDates) {
        this.id = 0;
        this.bus = bus;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.start = start;
        this.duration = duration;
        this.price = price;
        this.approved = approved;
        this.tripDates = tripDates;
    }
}

package com.gladun.buscompany.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TripDate {

    private int id;
    private LocalDate date;
    private int freePlaces;
    private List<Order> orders;
    private List<Place> places;

    public TripDate(int id, LocalDate date, int freePlaces, List<Order> orders, List<Place> places) {
        this(date, freePlaces, orders, places);
        this.id = id;
    }

    public TripDate(LocalDate date, int freePlaces, List<Order> orders, List<Place> places) {
        this.id = 0;
        this.date = date;
        this.freePlaces = freePlaces;
        this.orders = orders;
        this.places = places;
    }

    public TripDate(LocalDate date) {
        this.date = date;
    }
}

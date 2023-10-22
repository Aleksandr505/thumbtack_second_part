package com.gladun.buscompany.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Place {

    private int id;
    private int placeNumber;
    private Passenger passenger;
    private TripDate tripDate;

    public Place(int id, int placeNumber, Passenger passenger, TripDate tripDate) {
        this(placeNumber, passenger, tripDate);
        this.id = id;
    }

    public Place(int placeNumber, Passenger passenger, TripDate tripDate) {
        this.id = 0;
        this.placeNumber = placeNumber;
        this.passenger = passenger;
        this.tripDate = tripDate;
    }
}

package com.gladun.buscompany.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Order {

    private int id;
    private TripDate tripDate;
    private Client client;
    private List<Passenger> passengers;

    public Order(int id, TripDate tripDate, Client client, List<Passenger> passengers) {
        this(tripDate, client, passengers);
        this.id = id;
    }

    public Order(TripDate tripDate, Client client, List<Passenger> passengers) {
        this.id = 0;
        this.tripDate = tripDate;
        this.client = client;
        this.passengers = passengers;
    }
}

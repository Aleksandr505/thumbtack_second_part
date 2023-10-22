package com.gladun.buscompany.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Passenger {

    private int id;
    private String firstName;
    private String lastName;
    private String passport;
    private List<Order> order;

    public Passenger(int id, String firstName, String lastName, String passport, List<Order> order) {
        this(firstName, lastName, passport, order);
        this.id = id;
    }

    public Passenger(String firstName, String lastName, String passport, List<Order> order) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.order = order;
    }
}

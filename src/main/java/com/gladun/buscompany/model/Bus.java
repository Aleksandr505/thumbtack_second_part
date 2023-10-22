package com.gladun.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    private int id;
    private String busName;
    private int placeCount;

    public Bus(String busName, int placeCount) {
        this.id = 0;
        this.busName = busName;
        this.placeCount = placeCount;
    }
}

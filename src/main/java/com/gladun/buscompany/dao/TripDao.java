package com.gladun.buscompany.dao;

import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.Bus;
import com.gladun.buscompany.model.Trip;
import com.gladun.buscompany.model.TripDate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TripDao {

    Trip insert(Trip trip) throws ServerException;

    void update(Trip trip) throws ServerException;

    Integer updateTripDate(TripDate tripDate) throws ServerException;

    void deleteById(int id) throws ServerException;

    Trip getById(int id) throws ServerException;

    void approveTrip(int id) throws ServerException;

    Bus getBusByName(String busName) throws ServerException;

    List<Trip> getAllWithParams(String fromStation, String toStation, String busName, LocalDate fromDate, LocalDate toDate, boolean isClient) throws ServerException;

    Trip getTripByTripDate(TripDate tripDate) throws ServerException;
}

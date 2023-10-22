package com.gladun.buscompany.dao;

import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.Order;
import com.gladun.buscompany.model.Passenger;
import com.gladun.buscompany.model.Place;
import com.gladun.buscompany.model.TripDate;

import java.util.List;

public interface PlaceDao {

    void insertPlaces(List<Place> places) throws ServerException;

    List<Place> getAvailablePlaces(int orderId) throws ServerException;

    Integer updatePlaceForPassenger(Place place) throws ServerException;

    Place getPlaceByPassengerAndOrderId(Passenger passenger, int orderId) throws ServerException;

    void deletePlacesByTripDates(List<TripDate> tripDates) throws ServerException;

    void freeTheSeatsByTripDate(TripDate tripDate) throws ServerException;

}

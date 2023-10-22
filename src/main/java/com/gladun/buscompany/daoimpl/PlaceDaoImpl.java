package com.gladun.buscompany.daoimpl;

import com.gladun.buscompany.dao.PlaceDao;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mappers.PlaceMapper;
import com.gladun.buscompany.model.Passenger;
import com.gladun.buscompany.model.Place;
import com.gladun.buscompany.model.TripDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaceDaoImpl implements PlaceDao {

    private final PlaceMapper placeMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceDaoImpl.class);

    @Autowired
    public PlaceDaoImpl(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    @Override
    public void insertPlaces(List<Place> places) throws ServerException {
        LOGGER.debug("DAO insert Places {}", places);
        try {
            placeMapper.insertPlaces(places);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert Places {}", places, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<Place> getAvailablePlaces(int orderId) throws ServerException {
        LOGGER.debug("DAO get all available places on order id: {}", orderId);
        try {
            return placeMapper.getAllAvailablePlaces(orderId);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all available places on order id: {}", orderId, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Integer updatePlaceForPassenger(Place place) throws ServerException {
        LOGGER.debug("DAO update Place {}", place.getId());
        try {
            return placeMapper.updatePlaceForPassenger(place);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't update Place {}", place.getId(), ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Place getPlaceByPassengerAndOrderId(Passenger passenger, int orderId) throws ServerException {
        LOGGER.debug("DAO get Place by Passenger id: {}, and Order id: {}", passenger.getId(), orderId);
        try {
            return placeMapper.getPlaceByPassengerAndOrderId(passenger, orderId);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Place by Passenger id: {}, and Order id: {}", passenger.getId(), orderId, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void deletePlacesByTripDates(List<TripDate> tripDates) throws ServerException {
        LOGGER.debug("DAO delete Places by TripDates");
        try {
            for (TripDate tripDate : tripDates) {
                placeMapper.deletePlacesByTripDate(tripDate);
            }
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Places by TripDates", ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void freeTheSeatsByTripDate(TripDate tripDate) throws ServerException {
        LOGGER.debug("DAO free the Places by TripDate: {}", tripDate.getId());
        try {
            placeMapper.freeTheSeatsByTripDate(tripDate);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't free the Places by TripDate: {}", tripDate.getId(), ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }
}

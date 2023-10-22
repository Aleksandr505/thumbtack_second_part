package com.gladun.buscompany.daoimpl;

import com.gladun.buscompany.dao.TripDao;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mappers.TripMapper;
import com.gladun.buscompany.model.Bus;
import com.gladun.buscompany.model.Trip;
import com.gladun.buscompany.model.TripDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class TripDaoImpl implements TripDao {

    private final TripMapper tripMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TripDaoImpl.class);

    @Autowired
    public TripDaoImpl(TripMapper tripMapper) {
        this.tripMapper = tripMapper;
    }


    @Override
    public Trip insert(Trip trip) throws ServerException {
        LOGGER.debug("DAO insert Trip {}", trip);
        try {
            tripMapper.insert(trip);
            tripMapper.insertTripDates(trip.getTripDates(), trip);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert Trip {}", trip, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
        return trip;
    }

    @Override
    public void update(Trip trip) throws ServerException {
        LOGGER.debug("DAO change Trip {}", trip.getId());
        try {
            tripMapper.update(trip);
            tripMapper.deleteTripDates();
            tripMapper.insertTripDates(trip.getTripDates(), trip);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't change Trip {}", trip.getId(), ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Integer updateTripDate(TripDate tripDate) throws ServerException {
        LOGGER.debug("DAO change TripDate {}", tripDate.getId());
        try {
            return tripMapper.updateTripDate(tripDate);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't change TripDate {}", tripDate.getId(), ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void deleteById(int id) throws ServerException {
        LOGGER.debug("DAO delete Trip by Id {} ", id);
        try {
            tripMapper.deleteById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Trip by Id {}", id, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Trip getById(int id) throws ServerException {
        LOGGER.debug("DAO get Trip by Id {}", id);
        try {
            return tripMapper.getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Trip by Id {}", id, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void approveTrip(int id) throws ServerException {
        LOGGER.debug("DAO approve Trip with Id {}", id);
        try {
            tripMapper.approveTrip(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't approve Trip with Id {}", id, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Bus getBusByName(String busName) throws ServerException {
        LOGGER.debug("DAO get Bus by BusName {}", busName);
        try {
            return tripMapper.getBusByName(busName);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Bus by BusName {}", busName, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<Trip> getAllWithParams(String fromStation, String toStation, String busName, LocalDate fromDate, LocalDate toDate, boolean isClient) throws ServerException {
        LOGGER.debug("DAO get all Trips with params");
        try {
            return tripMapper.getAllWithParams(fromStation, toStation, busName, fromDate, toDate, isClient);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Trips with params", ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Trip getTripByTripDate(TripDate tripDate) throws ServerException {
        LOGGER.debug("DAO get Trip by TripDate {}", tripDate.getId());
        try {
            return tripMapper.getTripByTripDate(tripDate);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Trip by TripDate {}", tripDate.getId(), ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }


}

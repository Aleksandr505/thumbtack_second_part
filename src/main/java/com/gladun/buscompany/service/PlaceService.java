package com.gladun.buscompany.service;

import com.gladun.buscompany.dao.OrderDao;
import com.gladun.buscompany.dao.PlaceDao;
import com.gladun.buscompany.dao.SessionDao;
import com.gladun.buscompany.dao.TripDao;
import com.gladun.buscompany.dto.request.ChoicePlaceDtoRequest;
import com.gladun.buscompany.dto.response.ChoicePlaceDtoResponse;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.Passenger;
import com.gladun.buscompany.model.Place;
import com.gladun.buscompany.model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PlaceService {

    private final TripDao tripDao;
    private final OrderDao orderDao;
    private final PlaceDao placeDao;
    private final SessionDao sessionDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceService.class);

    public PlaceService(TripDao tripDao, OrderDao orderDao, PlaceDao placeDao, SessionDao sessionDao) {
        this.tripDao = tripDao;
        this.orderDao = orderDao;
        this.placeDao = placeDao;
        this.sessionDao = sessionDao;
    }

    @Transactional
    public List<Integer> getAvailablePlaces(int orderId, String cookie) throws ServerException {
        ServiceUtils.getClientUserByCookie(cookie, sessionDao);

        List<Place> places = placeDao.getAvailablePlaces(orderId);
        List<Integer> numberOfPlaces = new ArrayList<>();
        for (Place place : places) {
            numberOfPlaces.add(place.getPlaceNumber());
        }
        return numberOfPlaces;
    }

    @Transactional
    public ChoicePlaceDtoResponse choosePlace(ChoicePlaceDtoRequest request, String cookie) throws ServerException, OptimisticLockException {
        ServiceUtils.getClientUserByCookie(cookie, sessionDao);

        List<Passenger> passengers = orderDao.getPassengersByOrderId(request.getOrderId());
        //LOGGER.info("DB passengers by orderId {}: {}", request.getOrderId(), passengers);
        Passenger currentPassenger = null;
        for (Passenger passenger : passengers) {
            //LOGGER.info("Passenger: {}", passenger);
            if (request.getFirstName().equals(passenger.getFirstName()) && request.getLastName().equals(passenger.getLastName()) && request.getPassport().equals(passenger.getPassport())) {
                currentPassenger = passenger;
            }
        }
        if (currentPassenger == null)
            throw new ServerException(ServerErrorCode.PASSENGER_NOT_FOUND);

        List<Place> places = placeDao.getAvailablePlaces(request.getOrderId());
        Place currentPlace;
        Comparator<Place> placeNumberComp = new Comparator<Place>() {
            @Override
            public int compare(Place o1, Place o2) {
                return Integer.compare(o1.getPlaceNumber(), o2.getPlaceNumber());
            }
        };
        int index = Collections.binarySearch(places, new Place(request.getPlace(), null, null), placeNumberComp);
        if (index >= 0) {
            currentPlace = places.get(index);
        } else {
            throw new ServerException(ServerErrorCode.PLACE_NOT_FOUND);
        }

        currentPlace.setPassenger(currentPassenger);
        LOGGER.info("Current Place id: {}", currentPlace.getId());
        int isPlaceUpdated = placeDao.updatePlaceForPassenger(currentPlace);
        if (isPlaceUpdated == 0)
            throw new OptimisticLockException("Не удалось занять место! Попробуйте снова получить список свободных мест и повторите попытку");

        Place oldPlace = placeDao.getPlaceByPassengerAndOrderId(currentPassenger, request.getOrderId());
        if (oldPlace != null) {
            oldPlace.setPassenger(null);
            placeDao.updatePlaceForPassenger(oldPlace);
        }

        Trip trip = tripDao.getTripByTripDate(currentPlace.getTripDate());
        String ticket = "Билет " + trip.getId() + "_" + currentPlace.getPlaceNumber();
        return new ChoicePlaceDtoResponse(request.getOrderId(), ticket,
                currentPassenger.getLastName(), currentPassenger.getFirstName(), currentPassenger.getPassport(),
                currentPlace.getPlaceNumber());
    }

}

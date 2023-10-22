package com.gladun.buscompany.service;

import com.gladun.buscompany.dao.*;
import com.gladun.buscompany.dto.PassengerDto;
import com.gladun.buscompany.dto.request.OrderDtoRequest;
import com.gladun.buscompany.dto.response.OrderDtoResponse;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mapstruct.OrderMapStruct;
import com.gladun.buscompany.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final ClientDao clientDao;
    private final SessionDao sessionDao;
    private final TripDao tripDao;
    private final PlaceDao placeDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderDao orderDao, ClientDao clientDao, SessionDao sessionDao, TripDao tripDao, PlaceDao placeDao) {
        this.orderDao = orderDao;
        this.clientDao = clientDao;
        this.sessionDao = sessionDao;
        this.tripDao = tripDao;
        this.placeDao = placeDao;
    }

    @Transactional
    public OrderDtoResponse addOrder(OrderDtoRequest orderDtoRequest, String cookie) throws ServerException, OptimisticLockException {
        User user = ServiceUtils.getClientUserByCookie(cookie, sessionDao);
        Client client = clientDao.getClientByUser(user);
        Trip trip = tripDao.getById(orderDtoRequest.getTripId());

        LocalDate date = LocalDate.parse(orderDtoRequest.getDate());
        TripDate orderDate;
        Comparator<TripDate> dateComp = new Comparator<TripDate>() {
            @Override
            public int compare(TripDate o1, TripDate o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
        int index = Collections.binarySearch(trip.getTripDates(), new TripDate(date), dateComp);
        if (index >= 0) {
            orderDate = trip.getTripDates().get(index);
        } else {
            throw new ServerException(ServerErrorCode.TRIP_DATE_NOT_FOUND);
        }

        List<Passenger> passengers = new ArrayList<>();
        for (PassengerDto dto : orderDtoRequest.getPassengers()) {
            passengers.add(OrderMapStruct.INSTANCE.fromPassengerDtoToPassenger(dto));
        }

        Order order = new Order();
        order.setTripDate(orderDate);
        order.setClient(client);
        order.setPassengers(passengers);

        int newFreePlaces = orderDate.getFreePlaces() - orderDtoRequest.getPassengers().size();
        orderDate.setFreePlaces(newFreePlaces);
        int countUpdateTripDate = tripDao.updateTripDate(orderDate);
        if (countUpdateTripDate == 0)
            throw new OptimisticLockException("Не удалось сделать заказ, возможно не хватило свободных мест. Попробуйте снова");

        orderDao.insert(order);

        return createOrderDtoResponse(order, trip);
    }

    @Transactional
    public List<OrderDtoResponse> getOrdersByParams(String fromStation, String toStation, String busName,
                                                    String fromDate, String toDate, String clientId, String cookie) throws ServerException {
        User user = sessionDao.getUserByCookie(cookie);
        if (user == null)
            throw new ServerException(ServerErrorCode.WRONG_COOKIE);

        int clientIdParam = 0;
        if (user.getUserType() == RoleEnum.CLIENT) {
            clientIdParam = user.getId();
        } else if (clientId != null) {
            clientIdParam = Integer.parseInt(clientId);
        }

        LocalDate fromDateObj = null;
        LocalDate toDateObj = null;
        if (fromDate != null && !fromDate.isEmpty())
            fromDateObj = LocalDate.parse(fromDate);
        if (toDate != null && !toDate.isEmpty())
            toDateObj = LocalDate.parse(toDate);

        List<Order> orders = orderDao.getAllWithParams(fromStation, toStation, busName, fromDateObj, toDateObj, clientIdParam);

        //LOGGER.debug("Get Orders by params: {}", orders);

        List<OrderDtoResponse> responseList = new ArrayList<>();
        for (Order order : orders) {
            Trip trip = tripDao.getTripByTripDate(order.getTripDate());
            OrderDtoResponse response = createOrderDtoResponse(order, trip);
            responseList.add(response);
        }


        return responseList;
    }

    public void cancelOrder(int orderId, String cookie) throws ServerException {
        ServiceUtils.getClientUserByCookie(cookie, sessionDao);

        Order order = orderDao.getById(orderId);
        placeDao.freeTheSeatsByTripDate(order.getTripDate());

        orderDao.deleteById(orderId);
    }

    private static OrderDtoResponse createOrderDtoResponse(Order order, Trip trip) {
        List<PassengerDto> passengerDtos = new ArrayList<>();
        for (Passenger passenger : order.getPassengers()) {
            passengerDtos.add(OrderMapStruct.INSTANCE.fromPassengerToPassengerDto(passenger));
        }
        int basePrice = Integer.parseInt(trip.getPrice().replaceAll("\\s+руб\\.", ""));
        int totalPrice = basePrice * passengerDtos.size();
        OrderDtoResponse response = OrderMapStruct.INSTANCE.fromOrderToOrderDtoResponse(order, trip);
        response.setTotalPrice(totalPrice + " руб.");
        response.setPassengers(passengerDtos);
        LOGGER.info("OrderDtoResponse: {}", response);
        return response;
    }

}

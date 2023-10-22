package com.gladun.buscompany.dao;

import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.Order;
import com.gladun.buscompany.model.Passenger;
import com.gladun.buscompany.model.TripDate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderDao {

    Order insert(Order order) throws ServerException;

    List<Order> getAllWithParams(String fromStation, String toStation, String busName, LocalDate fromDate, LocalDate toDate, int clientId) throws ServerException;

    void deleteById(int id) throws ServerException;

    List<Passenger> getPassengersByOrderId(int orderId) throws ServerException;

    Order getById(int id) throws ServerException;

}

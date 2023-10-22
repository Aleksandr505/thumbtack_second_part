package com.gladun.buscompany.daoimpl;

import com.gladun.buscompany.dao.OrderDao;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mappers.OrderMapper;
import com.gladun.buscompany.model.Order;
import com.gladun.buscompany.model.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final OrderMapper orderMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class);

    @Autowired
    public OrderDaoImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public Order insert(Order order) throws ServerException {
        LOGGER.debug("DAO insert Order with TripDate id: {}, Client id: {}, passengers size: {}", order.getTripDate().getId(),
                order.getClient().getUser().getId(), order.getPassengers().size());
        try {
            orderMapper.insert(order);
            orderMapper.insertPassengers(order.getPassengers());
            orderMapper.insertPassengerOrder(order);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert Order with TripDate id: {}, Client id: {}, passengers size: {}", order.getTripDate().getId(),
                    order.getClient().getUser().getId(), order.getPassengers().size(), ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
        return order;
    }

    public List<Order> getAllWithParams(String fromStation, String toStation, String busName, LocalDate fromDate, LocalDate toDate, int clientId) throws ServerException {
        LOGGER.debug("DAO get all Orders with params");
        try {
            return orderMapper.getAllWithParams(fromStation, toStation, busName, fromDate, toDate, clientId);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Orders with params", ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void deleteById(int id) throws ServerException {
        LOGGER.debug("DAO delete Order by ID {}", id);
        try {
            orderMapper.deleteById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Order by ID {}", id, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<Passenger> getPassengersByOrderId(int orderId) throws ServerException {
        LOGGER.debug("DAO get passengers by orderId {}", orderId);
        try {
            return orderMapper.getPassengersByOrderId(orderId);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get passengers by orderId {}", orderId, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Order getById(int id) throws ServerException {
        LOGGER.debug("DAO get Order by id: {}", id);
        try {
            return orderMapper.getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Order by id: {}", id, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }
}

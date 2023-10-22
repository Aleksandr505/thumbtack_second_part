package com.gladun.buscompany.mapstruct;

import com.gladun.buscompany.dto.PassengerDto;
import com.gladun.buscompany.dto.request.OrderDtoRequest;
import com.gladun.buscompany.dto.response.OrderDtoResponse;
import com.gladun.buscompany.model.Order;
import com.gladun.buscompany.model.Passenger;
import com.gladun.buscompany.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapStruct {

    OrderMapStruct INSTANCE = Mappers.getMapper(OrderMapStruct.class);

    Order fromOrderDtoRequestToOrder(OrderDtoRequest orderDtoRequest);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "trip.id", target = "tripId")
    @Mapping(source = "trip.fromStation", target = "fromStation")
    @Mapping(source = "trip.toStation", target = "toStation")
    @Mapping(source = "trip.bus.busName", target = "busName")
    @Mapping(source = "order.tripDate.date", target = "date")
    @Mapping(source = "trip.start", target = "start")
    @Mapping(source = "trip.duration", target = "duration")
    @Mapping(source = "trip.price", target = "price")
    OrderDtoResponse fromOrderToOrderDtoResponse(Order order, Trip trip);

    Passenger fromPassengerDtoToPassenger(PassengerDto passengerDto);

    PassengerDto fromPassengerToPassengerDto(Passenger passenger);

}

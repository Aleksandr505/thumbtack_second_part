package com.gladun.buscompany.mapstruct;

import com.gladun.buscompany.dto.request.TripDtoRequest;
import com.gladun.buscompany.dto.response.TripDtoResponse;
import com.gladun.buscompany.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TripMapStruct {

    TripMapStruct INSTANCE = Mappers.getMapper(TripMapStruct.class);

    Trip fromTripDtoRequestToTrip(TripDtoRequest tripDtoRequest);

    @Mapping(source = "trip.id", target = "tripId")
    @Mapping(source = "trip.bus.busName", target = "busName")
    @Mapping(source = "trip.bus.placeCount", target = "placeCount")
    @Mapping(target = "approved", defaultValue = "false")
    TripDtoResponse fromTripToTripDtoResponse(Trip trip);

}

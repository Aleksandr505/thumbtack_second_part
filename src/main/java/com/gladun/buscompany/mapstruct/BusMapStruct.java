package com.gladun.buscompany.mapstruct;

import com.gladun.buscompany.dto.response.BusDtoResponse;
import com.gladun.buscompany.model.Bus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BusMapStruct {

    BusMapStruct INSTANCE = Mappers.getMapper(BusMapStruct.class);

    @Mapping(source = "bus.busName", target = "busName")
    @Mapping(source = "bus.placeCount", target = "placeCount")
    BusDtoResponse fromBusToBusDtoResponse(Bus bus);

}

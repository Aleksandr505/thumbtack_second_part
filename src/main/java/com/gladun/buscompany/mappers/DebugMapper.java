package com.gladun.buscompany.mappers;

import com.gladun.buscompany.model.Bus;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DebugMapper {

    @Delete("DELETE FROM user")
    void deleteAllFromUser();

    @Delete("DELETE FROM trip")
    void deleteAllFromTrip();

    @Delete("DELETE FROM passenger")
    void deleteAllFromPassenger();

    @Delete("DELETE FROM bus_depot")
    void deleteAllFromBuses();

    @Insert("INSERT INTO bus_depot ( busName, placeCount ) VALUES ( #{bus.busName}, #{bus.placeCount} )")
    void insertBus(@Param("bus")Bus bus);

}

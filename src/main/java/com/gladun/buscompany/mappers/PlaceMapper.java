package com.gladun.buscompany.mappers;

import com.gladun.buscompany.model.Bus;
import com.gladun.buscompany.model.Passenger;
import com.gladun.buscompany.model.Place;
import com.gladun.buscompany.model.TripDate;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface PlaceMapper {

    @Insert({"<script>",
            "INSERT INTO place ( trip_date_id, placeNumber ) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "( #{item.tripDate.id}, #{item.placeNumber} )",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "list.id")
    void insertPlaces(@Param("list") List<Place> places);

    @Select("SELECT place.id as placeId, place.trip_date_id as tripDateId, placeNumber FROM place join `order` on place.trip_date_id = `order`.trip_date_id " +
            "WHERE `order`.id = #{orderId} AND passenger_id IS NULL")
    @Results({
            @Result(property = "id", column = "placeId"),
            @Result(property = "placeNumber", column = "placeNumber"),
            @Result(property = "tripDate", column = "tripDateId", javaType = TripDate.class,
                    one = @One(select = "com.gladun.buscompany.mappers.TripMapper.getTripDateById", fetchType = FetchType.LAZY))})
    List<Place> getAllAvailablePlaces(@Param("orderId") int orderId);

    @Update("UPDATE place SET passenger_id = #{place.passenger.id} where id = #{place.id} AND passenger_id IS NULL")
    Integer updatePlaceForPassenger(@Param("place") Place place);

    @Select("SELECT * FROM place join `order` on place.trip_date_id = `order`.trip_date_id " +
            "WHERE `order`.id = #{orderId} AND passenger_id = #{passenger.id}")
    Place getPlaceByPassengerAndOrderId(@Param("passenger") Passenger passenger, @Param("orderId") int orderId);

    @Delete("DELETE FROM place where trip_date_id = #{tripDate.id}")
    void deletePlacesByTripDate(@Param("tripDate") TripDate tripDate);

    @Update("UPDATE place SET passenger_id = null where trip_date_id = #{tripDate.id}")
    void freeTheSeatsByTripDate(@Param("tripDate") TripDate tripDate);

    @Select("SELECT * FROM place where trip_date_id = #{tripDateId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "placeNumber", column = "placeNumber"),
            @Result(property = "tripDate", column = "trip_date_id", javaType = TripDate.class,
                    one = @One(select = "com.gladun.buscompany.mappers.TripMapper.getTripDateById", fetchType = FetchType.LAZY))})
    List<Place> getPlacesByTripDate(@Param("tripDateId") int tripDateId);

}

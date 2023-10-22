package com.gladun.buscompany.mappers;

import com.gladun.buscompany.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface TripMapper {

    @Insert("INSERT INTO trip ( busBrand_id, fromStation, toStation, `start`, duration, price, approved ) " +
            "VALUES ( #{trip.bus.id}, #{trip.fromStation}, #{trip.toStation}, #{trip.start}," +
            " #{trip.duration}, #{trip.price}, false )")
    @Options(useGeneratedKeys = true, keyProperty = "trip.id")
    Integer insert(@Param("trip") Trip trip);

    @Insert({"<script>",
            "INSERT INTO trip_date ( trip_id, `date`, freePlaces ) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "( #{trip.id}, #{item.date}, #{item.freePlaces} )",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "list.id")
    void insertTripDates(@Param("list") List<TripDate> tripDates, @Param("trip") Trip trip);

    @Delete("DELETE FROM trip_date;")
    void deleteTripDates();

    @Update("UPDATE trip SET busBrand_id = #{trip.bus.id}, fromStation = #{trip.fromStation}, " +
            "toStation = #{trip.toStation}, `start` = #{trip.start}, duration = #{trip.duration}, " +
            "price = #{trip.price} WHERE id = #{trip.id};")
    void update(@Param("trip") Trip trip);

    @Update("UPDATE trip_date SET freePlaces = #{tripDate.freePlaces} WHERE freePlaces >= #{tripDate.freePlaces} AND id = #{tripDate.id};")
    Integer updateTripDate(@Param("tripDate") TripDate tripDate);

    @Delete("DELETE FROM trip WHERE id = #{tripId}")
    void deleteById(@Param("tripId") int tripId);

    @Select("SELECT * FROM trip where id = #{id};")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "bus", column = "busBrand_id", javaType = Bus.class,
            one = @One(select = "com.gladun.buscompany.mappers.TripMapper.getBusById", fetchType = FetchType.LAZY)),
            @Result(property = "tripDates", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.TripMapper.getTripDatesByTrip", fetchType = FetchType.LAZY))})
    Trip getById(@Param("id") int id);

    @Update("UPDATE trip SET approved = true WHERE id = #{id};")
    void approveTrip(@Param("id") int id);

    @Select("SELECT * from bus_depot where busName = #{busName}")
    Bus getBusByName(@Param("busName") String busName);

    @Select({"<script>",
            "SELECT trip.id as tripId, busBrand_id, busName, placeCount, fromStation, toStation, `start`, duration, price, " +
                    "approved FROM trip JOIN bus_depot ON bus_depot.id = trip.busBrand_id",
            "<where>" ,
            "<if test=\"isClient\"> AND approved = true",
            "</if>",
            "<choose>",
            "<when test='fromStation != null'> AND fromStation like #{fromStation}",
            "</when>",
            "<when test='toStation != null'> AND toStation like #{toStation}",
            "</when>",
            "<when test='toStation != null and fromStation != null'> AND fromStation like #{fromStation} AND toStation like #{toStation}",
            "</when>",
            "<when test='busName != null'> AND busName like #{busName}",
            "</when>",
            "<when test='fromDate != null'> AND DATEDIFF(start, #{fromDate}) &gt; 0",
            "</when>",
            "<when test='toDate != null'> AND DATEDIFF(start, #{toDate}) &lt;= 0",
            "</when>",
            "</choose>",
            "</where>" ,
            "</script>"})
    @Results({
            @Result(property = "id", column = "tripId"),
            @Result(property = "bus", column = "busBrand_id", javaType = Bus.class,
                    one = @One(select = "com.gladun.buscompany.mappers.TripMapper.getBusById", fetchType = FetchType.LAZY)),
            @Result(property = "tripDates", column = "tripId", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.TripMapper.getTripDatesByTrip", fetchType = FetchType.LAZY))})
    List<Trip> getAllWithParams(@Param("fromStation") String fromStation, @Param("toStation") String toStation,
                                @Param("busName") String busName, @Param("fromDate") LocalDate fromDate,
                                @Param("toDate") LocalDate toDate, @Param("isClient") boolean isClient);

    @Select("SELECT * FROM trip_date WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "orders", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.OrderMapper.getOrdersByTripDate", fetchType = FetchType.LAZY)),
            @Result(property = "places", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.PlaceMapper.getPlacesByTripDate", fetchType = FetchType.LAZY))})
    TripDate getTripDateById(@Param("id") int id);

    @Select("SELECT * FROM trip_date WHERE trip_id = #{tripId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "orders", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.OrderMapper.getOrdersByTripDate", fetchType = FetchType.LAZY)),
            @Result(property = "places", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.PlaceMapper.getPlacesByTripDate", fetchType = FetchType.LAZY))})
    List<TripDate> getTripDatesByTrip(@Param("tripId") int tripId);

    @Select("SELECT * FROM bus_depot WHERE id = #{id}")
    Bus getBusById(@Param("id") int id);

    @Select("SELECT * FROM trip_date join trip on trip_id = trip.id WHERE trip_date.id = #{tripDate.id}")
    @Results({
            @Result(property = "id", column = "trip_id"),
            @Result(property = "bus", column = "busBrand_id", javaType = Bus.class,
                    one = @One(select = "com.gladun.buscompany.mappers.TripMapper.getBusById", fetchType = FetchType.LAZY)),
            @Result(property = "tripDates", column = "trip_id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.TripMapper.getTripDatesByTrip", fetchType = FetchType.LAZY))})
    Trip getTripByTripDate(@Param("tripDate") TripDate tripDate);
}

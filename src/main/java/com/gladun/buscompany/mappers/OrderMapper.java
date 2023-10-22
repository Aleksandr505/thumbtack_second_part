package com.gladun.buscompany.mappers;

import com.gladun.buscompany.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO `order` ( trip_date_id, client_id ) VALUES ( #{order.tripDate.id}, #{order.client.user.id} )")
    @Options(useGeneratedKeys = true, keyProperty = "order.id")
    Integer insert(@Param("order") Order order);

    @Insert({"<script>",
            "INSERT INTO passenger ( firstName, lastName, passport ) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "( #{item.firstName}, #{item.lastName}, #{item.passport} )",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "list.id")
    void insertPassengers(@Param("list") List<Passenger> passengers);

    @Insert({"<script>",
            "INSERT INTO passenger_order ( passenger_id, order_id ) VALUES",
            "<foreach item='item' collection='order.getPassengers' separator=','>",
            "( #{item.id}, #{order.id} )",
            "</foreach>",
            "</script>"})
    void insertPassengerOrder(@Param("order") Order order);

    @Select("SELECT * FROM `order` where id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "tripDate", column = "trip_date_id", javaType = TripDate.class,
                    one = @One(select = "com.gladun.buscompany.mappers.TripMapper.getTripDateById", fetchType = FetchType.LAZY)),
            @Result(property = "client", column = "client_id", javaType = Client.class,
                    one = @One(select = "com.gladun.buscompany.mappers.ClientMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "passengers", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.OrderMapper.getPassengersByOrderId", fetchType = FetchType.LAZY))})
    Order getById(@Param("id") int id);

    @Select({"<script>",
            "SELECT `order`.id as orderId, trip_date_id, trip_id, client_id FROM `order` " +
                    "JOIN trip_date ON trip_date.id = `order`.trip_date_id " +
                    "JOIN trip ON trip.id = trip_date.trip_id " +
                    "JOIN bus_depot ON bus_depot.id = trip.busBrand_id",
            "<where>" ,
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
            "<when test='clientId != 0'> AND client_id = #{clientId}",
            "</when>",
            "</choose>",
            "</where>" ,
            "</script>"})
    @Results({
            @Result(property = "id", column = "orderId"),
            @Result(property = "tripDate", column = "trip_date_id", javaType = TripDate.class,
                    one = @One(select = "com.gladun.buscompany.mappers.TripMapper.getTripDateById", fetchType = FetchType.LAZY)),
            @Result(property = "client", column = "client_id", javaType = Client.class,
                    one = @One(select = "com.gladun.buscompany.mappers.ClientMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "passengers", column = "orderId", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.OrderMapper.getPassengersByOrderId", fetchType = FetchType.LAZY))})
    List<Order> getAllWithParams(@Param("fromStation") String fromStation, @Param("toStation") String toStation,
                                 @Param("busName") String busName, @Param("fromDate") LocalDate fromDate,
                                 @Param("toDate") LocalDate toDate, @Param("clientId") int clientId);

    @Delete("DELETE FROM `order` WHERE id = #{orderId}")
    void deleteById(@Param("orderId") int orderId);

    @Select("SELECT * FROM passenger where id IN (SELECT passenger_id FROM passenger_order where order_id = #{orderId})")
    List<Passenger> getPassengersByOrderId(@Param("orderId") int orderId);

    @Select("SELECT * FROM `order` where trip_date_id = #{tripDateId}")
    @Results({
            @Result(property = "tripDate", column = "trip_date_id", javaType = TripDate.class,
                    one = @One(select = "com.gladun.buscompany.mappers.TripMapper.getTripDateById", fetchType = FetchType.LAZY)),
            @Result(property = "client", column = "client_id", javaType = Client.class,
                    one = @One(select = "com.gladun.buscompany.mappers.ClientMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "passengers", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.OrderMapper.getPassengersByOrderId", fetchType = FetchType.LAZY))})
    List<Order> getOrdersByTripDate(@Param("tripDateId") int tripDateId);

}

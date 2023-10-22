package com.gladun.buscompany.mappers;

import com.gladun.buscompany.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ClientMapper {

    @Insert("INSERT INTO user ( firstName, lastName, patronymic, login, password, user_type, version ) VALUES "
            + "( #{user.name}, #{user.surname}, #{user.patronymic}, #{user.login}, #{user.password}, 'CLIENT', 1 )")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer insertUser(@Param("user") User user);

    @Insert("INSERT INTO client ( user_id, email, phone ) VALUES ( #{client.user.id}, #{client.email}, #{client.phone} )")
    Integer insert(@Param("client") Client client);

    @Select("SELECT email, phone FROM client where user_id = #{user.id};")
    Client getClientByUser(@Param("user") User user);

    @Delete("DELETE FROM user WHERE id = #{client.id}")
    void delete(@Param("client") Client client);

    @Update("UPDATE user, client SET user.firstName = #{client.user.name}, user.lastName = #{client.user.surname}, " +
            "user.patronymic = #{client.user.patronymic}, user.password = #{client.user.password}, " +
            "user.version = #{client.user.version} + 1, client.email = #{client.email}, client.phone = #{client.phone} " +
            "WHERE user.id = #{client.user.id} AND user.version = #{client.user.version};")
    Integer update(@Param("client") Client client);

    @Select("SELECT id, user_id, email, phone FROM client where user_id = #{id};")
    @Results({
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "com.gladun.buscompany.mappers.SessionMapper.getUserById", fetchType = FetchType.LAZY)),
            @Result(property = "orders", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.ClientMapper.getOrdersByClient", fetchType = FetchType.LAZY))})
    Client getById(@Param("id") int id);

    @Select("SELECT * FROM `order` where client_id = #{clientId}")
    @Results({
            @Result(property = "tripDate", column = "trip_date_id", javaType = TripDate.class,
                    one = @One(select = "com.gladun.buscompany.mappers.OrderMapper.getTripDateById", fetchType = FetchType.LAZY)),
            @Result(property = "client", column = "client_id", javaType = Client.class,
                    one = @One(select = "com.gladun.buscompany.mappers.ClientMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "passengers", column = "id", javaType = List.class,
                    many = @Many(select = "com.gladun.buscompany.mappers.OrderMapper.getPassengersByOrderId", fetchType = FetchType.LAZY))})
    List<Order> getOrdersByClient(@Param("clientId") int clientId);
}

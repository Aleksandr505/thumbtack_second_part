package com.gladun.buscompany.mappers;

import com.gladun.buscompany.model.Admin;
import com.gladun.buscompany.model.Bus;
import com.gladun.buscompany.model.Client;
import com.gladun.buscompany.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Insert("INSERT INTO user ( firstName, lastName, patronymic, login, password, user_type, version ) VALUES "
            + "( #{user.name}, #{user.surname}, #{user.patronymic}, #{user.login}, #{user.password}, 'ADMIN', 1 )")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer insertUser(@Param("user") User user);

    @Insert("INSERT INTO admin ( user_id, position ) VALUES ( #{admin.user.id}, #{admin.position} )")
    Integer insert(@Param("admin") Admin admin);

    @Select("SELECT position FROM admin where user_id = #{user.id};")
    Admin getAdminByUser(@Param("user") User user);

    @Delete("DELETE FROM user WHERE id = #{admin.id}")
    void delete(@Param("admin") Admin admin);

    @Select("SELECT user.id, firstName as name, lastName as surname, patronymic, login, password, position " +
            "FROM user join admin ON user.id = admin.user_id where user.id = #{id};")
    Admin getById(@Param("id") int id);

    @Update("UPDATE user, admin SET user.firstName = #{admin.user.name}, user.lastName = #{admin.user.surname}, " +
            "user.patronymic = #{admin.user.patronymic}, user.password = #{admin.user.password}, " +
            "user.version = #{admin.user.version} + 1, admin.position = #{admin.position} " +
            "WHERE user.id = #{admin.user.id} AND user.version = #{admin.user.version};")
    Integer update(@Param("admin") Admin admin);

    @Select("SELECT * FROM bus_depot")
    List<Bus> getAllBuses();

    @Select("SELECT user_id, user.firstName as name, user.lastName as surname, " +
            "user.patronymic, client.email, client.phone FROM user join client ON user.id = client.user_id ORDER BY client.id")
    @Results({
            @Result(property = "user.id", column = "user_id"),
            @Result(property = "user.surname", column = "surname"),
            @Result(property = "user.name", column = "name"),
            @Result(property = "user.patronymic", column = "patronymic")})
    List<Client> getAllClients();

}

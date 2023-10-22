package com.gladun.buscompany.mappers;

import com.gladun.buscompany.model.Session;
import com.gladun.buscompany.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SessionMapper {

    @Insert("INSERT INTO session ( user_id, cookie ) VALUES ( #{session.user.id}, #{session.cookie} ) ON DUPLICATE KEY UPDATE cookie=VALUES(cookie);")
    Integer insertSession(@Param("session") Session session);

    @Select("SELECT id, firstName as name, lastName as surname, patronymic, login, password, user_type as userType, version FROM user where login = #{login};")
    User getUserByLogin(@Param("login") String login);

    @Select("SELECT id, firstName as name, lastName as surname, patronymic, login, password, user_type as userType, user.version, " +
            "user_id, cookie FROM user JOIN `session` ON id = user_id where cookie = #{cookie};")
    User getUserByCookie(@Param("cookie") String cookie);

    @Select("SELECT * FROM `session` where user_id = #{id};")
    Session getById(@Param("id") int id);

    @Update("UPDATE `session` SET cookie = #{session.cookie} WHERE user_id = #{session.user.id};")
    void update(@Param("session") Session session);

    @Delete("DELETE FROM `session` WHERE cookie = #{cookie}")
    void deleteByCookie(@Param("cookie") String cookie);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUserById(@Param("id") int id);

    @Select("SELECT id, firstName as name, lastName as surname, patronymic, login, password, user_type as userType, version " +
            "FROM user where user.id = #{id};")
    User getUserById(@Param("id") int id);

}

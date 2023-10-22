package com.gladun.buscompany.dao;

import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.Session;
import com.gladun.buscompany.model.User;

public interface SessionDao {

    Session insertCookie(Session session) throws ServerException;
    User getUserByCookie(String cookie) throws ServerException;
    Session getById(int id) throws ServerException;
    void update(Session session) throws ServerException;
    User getUserByLogin(String login) throws ServerException;
    void deleteByCookie(String cookie) throws ServerException;
    void deleteUserById(int id) throws ServerException;

}

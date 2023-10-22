package com.gladun.buscompany.dao;

import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.Client;
import com.gladun.buscompany.model.Session;
import com.gladun.buscompany.model.User;

public interface ClientDao {

    Client insert(Client client) throws ServerException;

    Client getByLogin(String login) throws ServerException;

    void delete(Client client) throws ServerException;

    Client getClientByUser(User user) throws ServerException;

    Integer update(Client client) throws ServerException;



}

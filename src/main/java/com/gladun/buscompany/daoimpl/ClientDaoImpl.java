package com.gladun.buscompany.daoimpl;

import com.gladun.buscompany.dao.ClientDao;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mappers.ClientMapper;
import com.gladun.buscompany.mappers.SessionMapper;
import com.gladun.buscompany.model.Client;
import com.gladun.buscompany.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;

@Repository
public class ClientDaoImpl implements ClientDao {

    private final ClientMapper clientMapper;
    private final SessionMapper sessionMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDaoImpl.class);

    @Autowired
    public ClientDaoImpl(ClientMapper clientMapper, SessionMapper sessionMapper) {
        this.clientMapper = clientMapper;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public Client insert(Client client) throws ServerException {
        LOGGER.debug("DAO insert Client {}", client);
        try {
            clientMapper.insertUser(client.getUser());
            clientMapper.insert(client);
        } catch (RuntimeException ex) {
            if (ex.getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                LOGGER.info("Can't insert Client {}", client);
                throw new ServerException(ServerErrorCode.LOGIN_BUSY);
            } else {
                LOGGER.info("Can't insert Client {}", client);
                throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
            }
        }
        return client;
    }

    @Override
    public Client getByLogin(String login) throws ServerException {
        LOGGER.debug("DAO get Client by Login {}", login);
        try {
            User user = sessionMapper.getUserByLogin(login);
            Client client = clientMapper.getClientByUser(user);
            if (client != null)
                client.setUser(user);
            return client;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Client by Login {}", login, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void delete(Client client) throws ServerException {
        LOGGER.debug("DAO delete Client {} ", client);
        try {
            clientMapper.delete(client);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Client {}", client, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Client getClientByUser(User user) throws ServerException {
        LOGGER.debug("DAO get Client by User {}", user);
        try {
            Client client = clientMapper.getClientByUser(user);
            if (client != null)
                client.setUser(user);
            return client;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Client by User {}", user, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Integer update(Client client) throws ServerException {
        LOGGER.debug("DAO change Client {}", client);
        try {
            return clientMapper.update(client);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't change Client {}", client, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }
}

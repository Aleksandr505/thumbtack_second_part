package com.gladun.buscompany.daoimpl;

import com.gladun.buscompany.dao.SessionDao;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mappers.SessionMapper;
import com.gladun.buscompany.model.Session;
import com.gladun.buscompany.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SessionDaoImpl implements SessionDao {

    private final SessionMapper sessionMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDaoImpl.class);

    @Autowired
    public SessionDaoImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    @Override
    public Session insertCookie(Session session) throws ServerException {
        LOGGER.debug("DAO insert Cookie for user {} {}", session.getCookie(), session.getUser());
            try {
                sessionMapper.insertSession(session);
                LOGGER.debug("Cookie inserted for user {} {}", session.getCookie(), session.getUser());
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Cookie for user {} {}", session.getCookie(), session.getUser(), ex);
                throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
            }
        return session;
    }

    @Override
    public User getUserByCookie(String cookie) throws ServerException {
        LOGGER.debug("DAO get Session by Cookie {}", cookie);
        try {
            return sessionMapper.getUserByCookie(cookie);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Session by Cookie {}", cookie, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Session getById(int id) throws ServerException {
        LOGGER.debug("DAO get Session by Id {}", id);
        try {
            return sessionMapper.getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Session by Id {}", id, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public User getUserByLogin(String login) throws ServerException {
        LOGGER.debug("DAO get User by Login {}", login);
        try {
            return sessionMapper.getUserByLogin(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get User by Login {}", login, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void update(Session session) throws ServerException {
        LOGGER.debug("DAO change Session {}", session);
        try {
            sessionMapper.update(session);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't change Session {}", session, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void deleteByCookie(String cookie) throws ServerException {
        LOGGER.debug("DAO delete Session by Cookie {} ", cookie);
        try {
            sessionMapper.deleteByCookie(cookie);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Session by Cookie {}", cookie, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void deleteUserById(int id) throws ServerException {
        LOGGER.debug("DAO delete User by Id {} ", id);
        try {
            sessionMapper.deleteUserById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete User by Id {} ", id, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }
}

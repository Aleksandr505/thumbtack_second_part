package com.gladun.buscompany.daoimpl;

import com.gladun.buscompany.dao.AdminDao;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mappers.AdminMapper;
import com.gladun.buscompany.mappers.SessionMapper;
import com.gladun.buscompany.model.Admin;
import com.gladun.buscompany.model.Bus;
import com.gladun.buscompany.model.Client;
import com.gladun.buscompany.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Repository
public class AdminDaoImpl implements AdminDao {

    private final AdminMapper adminMapper;
    private final SessionMapper sessionMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Autowired
    public AdminDaoImpl(AdminMapper adminMapper, SessionMapper sessionMapper) {
        this.adminMapper = adminMapper;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public Admin insert(Admin admin) throws ServerException {
        LOGGER.debug("DAO insert Admin {}", admin);
        try {
            adminMapper.insertUser(admin.getUser());
            adminMapper.insert(admin);
        } catch (RuntimeException ex) {
            if (ex.getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                LOGGER.info("Can't insert Admin {}", admin);
                throw new ServerException(ServerErrorCode.LOGIN_BUSY);
            } else {
                LOGGER.info("Can't insert Admin {}", admin);
                throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
            }
        }
        return admin;
    }

    @Override
    public Admin getByLogin(String login) throws ServerException {
        LOGGER.debug("DAO get Admin by Login {}", login);
        try {
            User user = sessionMapper.getUserByLogin(login);
            Admin admin = adminMapper.getAdminByUser(user);
            if (admin != null)
                admin.setUser(user);
            return admin;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Admin by Login {}", login, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public void delete(Admin admin) throws ServerException {
        LOGGER.debug("DAO delete Admin {} ", admin);
        try {
            adminMapper.delete(admin);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Admin {}", admin, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Admin getAdminByUser(User user) throws ServerException {
        LOGGER.debug("DAO get Admin by User {}", user);
        try {
            Admin admin = adminMapper.getAdminByUser(user);
            if (admin != null)
                admin.setUser(user);
            return admin;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Admin by User {}", user, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Integer update(Admin admin) throws ServerException {
        LOGGER.debug("DAO change Admin {}", admin);
        try {
            return adminMapper.update(admin);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't change Admin {}", admin, ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<Bus> getAllBuses() throws ServerException {
        LOGGER.debug("DAO get all Buses");
        try {
            return adminMapper.getAllBuses();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get All Buses", ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public List<Client> getAllClients() throws ServerException {
        LOGGER.debug("DAO get all Clients");
        try {
            return adminMapper.getAllClients();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get All Clients", ex);
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }
}

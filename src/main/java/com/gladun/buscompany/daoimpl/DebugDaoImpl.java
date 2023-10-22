package com.gladun.buscompany.daoimpl;

import com.gladun.buscompany.dao.DebugDao;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mappers.DebugMapper;
import com.gladun.buscompany.model.Bus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;

@Repository
public class DebugDaoImpl implements DebugDao {

    private final DebugMapper debugMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugDaoImpl.class);

    @Autowired
    public DebugDaoImpl(DebugMapper debugMapper) {
        this.debugMapper = debugMapper;
    }

    @Override
    public void clearDatabase() throws ServerException {
        LOGGER.debug("Clear Database");
        try {
            debugMapper.deleteAllFromUser();
            debugMapper.deleteAllFromTrip();
            debugMapper.deleteAllFromPassenger();
            debugMapper.deleteAllFromBuses();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't clear database");
            throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
        }
    }

    @Override
    public Bus insertBus(Bus bus) throws ServerException {
        LOGGER.debug("DAO insert Bus {}", bus);
        try {
            debugMapper.insertBus(bus);
        } catch (RuntimeException ex) {
            if (ex.getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                LOGGER.info("Can't insert Bus {}", bus);
                throw new ServerException(ServerErrorCode.LOGIN_BUSY);
            } else {
                LOGGER.info("Can't insert Bus {}", bus);
                throw new ServerException(ServerErrorCode.UNEXPECTED_EXCEPTION);
            }
        }
        return bus;
    }
}

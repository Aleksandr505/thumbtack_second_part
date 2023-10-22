package com.gladun.buscompany.dao;

import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.Bus;

public interface DebugDao {

    void clearDatabase() throws ServerException;

    Bus insertBus(Bus bus) throws ServerException;

}

package com.gladun.buscompany.dao;

import com.gladun.buscompany.dto.response.ClientDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.*;

import java.util.List;

public interface AdminDao {

    Admin insert(Admin admin) throws ServerException;

    Admin getByLogin(String login) throws ServerException;

    void delete(Admin admin) throws ServerException;

    Admin getAdminByUser(User user) throws ServerException;

    Integer update(Admin admin) throws ServerException;

    List<Bus> getAllBuses() throws ServerException;

    List<Client> getAllClients() throws ServerException;

}

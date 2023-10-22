package com.gladun.buscompany.service;

import com.gladun.buscompany.dao.AdminDao;
import com.gladun.buscompany.dao.ClientDao;
import com.gladun.buscompany.dao.SessionDao;
import com.gladun.buscompany.dto.request.LoginDtoRequest;
import com.gladun.buscompany.dto.response.UserDtoResponse;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mapstruct.AdminMapStruct;
import com.gladun.buscompany.mapstruct.ClientMapStruct;
import com.gladun.buscompany.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class SessionService {

    private final AdminDao adminDao;
    private final ClientDao clientDao;
    private final SessionDao sessionDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    public SessionService(AdminDao adminDao, ClientDao clientDao, SessionDao sessionDao) {
        this.adminDao = adminDao;
        this.clientDao = clientDao;
        this.sessionDao = sessionDao;
    }

    @Transactional
    public UserDtoResponse login(LoginDtoRequest loginDtoRequest, HttpServletResponse httpServletResponse) throws ServerException {
        User user = sessionDao.getUserByLogin(loginDtoRequest.getLogin());
        if (user == null)
            throw new ServerException(ServerErrorCode.USER_NOT_FOUND);
        if (!user.getPassword().equals(loginDtoRequest.getPassword()))
            throw new ServerException(ServerErrorCode.WRONG_PASSWORD);

        Session session = new Session();
        session.setUser(user);
        session.setCookie(UUID.randomUUID().toString());
        sessionDao.insertCookie(session);
        ServiceUtils.addCookie(httpServletResponse, session);

        LOGGER.debug("USER : {}", user);

        if (user.getUserType() == RoleEnum.ADMIN) {
            Admin admin = adminDao.getByLogin(loginDtoRequest.getLogin());
            return AdminMapStruct.INSTANCE.fromAdminToAdminDtoResponse(admin);
        } else {
            Client client = clientDao.getByLogin(loginDtoRequest.getLogin());
            return ClientMapStruct.INSTANCE.fromClientToClientDtoResponse(client);
        }

    }

    public void logout(String cookie) throws ServerException {
        sessionDao.deleteByCookie(cookie);
    }

    @Transactional
    public void deleteUser(String cookie) throws ServerException {
        User user = sessionDao.getUserByCookie(cookie);
        sessionDao.deleteUserById(user.getId());
    }

    @Transactional
    public UserDtoResponse getUserData(String cookie) throws ServerException {
        User user = sessionDao.getUserByCookie(cookie);
        if (user == null)
            throw new ServerException(ServerErrorCode.USER_NOT_FOUND);

        if (user.getUserType() == RoleEnum.ADMIN) {
            Admin admin = adminDao.getByLogin(user.getLogin());
            return AdminMapStruct.INSTANCE.fromAdminToAdminDtoResponse(admin);
        } else {
            Client client = clientDao.getByLogin(user.getLogin());
            return ClientMapStruct.INSTANCE.fromClientToClientDtoResponse(client);
        }
    }

}

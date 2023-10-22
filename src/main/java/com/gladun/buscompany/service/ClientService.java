package com.gladun.buscompany.service;

import com.gladun.buscompany.dao.ClientDao;
import com.gladun.buscompany.dao.SessionDao;
import com.gladun.buscompany.dto.request.EditingAdminDtoRequest;
import com.gladun.buscompany.dto.request.EditingClientDtoRequest;
import com.gladun.buscompany.dto.request.RegisterClientDtoRequest;
import com.gladun.buscompany.dto.response.ClientDtoResponse;
import com.gladun.buscompany.dto.response.EditingAdminDtoResponse;
import com.gladun.buscompany.dto.response.EditingClientDtoResponse;
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

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientDao clientDao;
    private final SessionDao sessionDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    public ClientService(ClientDao clientDao, SessionDao sessionDao) {
        this.clientDao = clientDao;
        this.sessionDao = sessionDao;
    }

    @Transactional
    public ClientDtoResponse register(RegisterClientDtoRequest registerClientDtoRequest, HttpServletResponse httpServletResponse) throws ServerException {
        Client client = ClientMapStruct.INSTANCE.toClient(registerClientDtoRequest);
        clientDao.insert(client);

        String validPhone = client.getPhone().replaceAll("-", "");
        client.setPhone(validPhone);

        Session session = new Session();
        session.setUser(client.getUser());
        session.setCookie(UUID.randomUUID().toString());
        sessionDao.insertCookie(session);
        ServiceUtils.addCookie(httpServletResponse, session);

        return ClientMapStruct.INSTANCE.fromClientToClientDtoResponse(client);
    }

    @Transactional
    public EditingClientDtoResponse updateClient(EditingClientDtoRequest editRequest, String cookie) throws ServerException, OptimisticLockException {
        User user = ServiceUtils.getClientUserByCookie(cookie, sessionDao);

        if (editRequest.getOldPassword().equals(editRequest.getNewPassword()))
            throw new ServerException(ServerErrorCode.EDIT_PASSWORD_ERROR);

        Client client = clientDao.getClientByUser(user);
        client.getUser().setSurname(editRequest.getSurname());
        client.getUser().setName(editRequest.getName());
        client.getUser().setPatronymic(editRequest.getPatronymic());
        client.getUser().setPassword(editRequest.getNewPassword());
        client.setEmail(editRequest.getEmail());
        client.setPhone(editRequest.getPhone());
        int countUpdatedClients = clientDao.update(client);
        if (countUpdatedClients == 0)
            throw new OptimisticLockException("Не удалось обновить данные клиента, попробуйте снова!");
        return ClientMapStruct.INSTANCE.fromClientToEditingClientDtoResponse(client);
    }

}

package com.gladun.buscompany.service;

import com.gladun.buscompany.dao.AdminDao;
import com.gladun.buscompany.dao.SessionDao;
import com.gladun.buscompany.dto.request.EditingAdminDtoRequest;
import com.gladun.buscompany.dto.request.RegisterAdminDtoRequest;
import com.gladun.buscompany.dto.response.AdminDtoResponse;
import com.gladun.buscompany.dto.response.BusDtoResponse;
import com.gladun.buscompany.dto.response.ClientDtoResponse;
import com.gladun.buscompany.dto.response.EditingAdminDtoResponse;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mapstruct.AdminMapStruct;
import com.gladun.buscompany.mapstruct.BusMapStruct;
import com.gladun.buscompany.mapstruct.ClientMapStruct;
import com.gladun.buscompany.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    private final AdminDao adminDao;
    private final SessionDao sessionDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    public AdminService(AdminDao adminDao, SessionDao sessionDao) {
        this.adminDao = adminDao;
        this.sessionDao = sessionDao;
    }


    @Transactional
    public AdminDtoResponse register(RegisterAdminDtoRequest registerAdminDtoRequest, HttpServletResponse httpServletResponse) throws ServerException {
        Admin admin = AdminMapStruct.INSTANCE.toAdmin(registerAdminDtoRequest);
        adminDao.insert(admin);

        Session session = new Session();
        session.setUser(admin.getUser());
        session.setCookie(UUID.randomUUID().toString());
        sessionDao.insertCookie(session);
        ServiceUtils.addCookie(httpServletResponse, session);

        return AdminMapStruct.INSTANCE.fromAdminToAdminDtoResponse(admin);
    }

    @Transactional
    public List<ClientDtoResponse> getAllClients(String cookie) throws ServerException {
        ServiceUtils.getAdminUserByCookie(cookie, sessionDao);

        List<Client> clients = adminDao.getAllClients();
        List<ClientDtoResponse> clientDtoResponses = new ArrayList<>();
        for (Client client : clients) {
            clientDtoResponses.add(ClientMapStruct.INSTANCE.fromClientToClientDtoResponse(client));
        }
        return clientDtoResponses;
    }

    @Transactional
    public EditingAdminDtoResponse updateAdmin(EditingAdminDtoRequest editRequest, String cookie) throws ServerException, OptimisticLockException {
        User user = ServiceUtils.getAdminUserByCookie(cookie, sessionDao);

        if (editRequest.getOldPassword().equals(editRequest.getNewPassword()))
            throw new ServerException(ServerErrorCode.EDIT_PASSWORD_ERROR);

        Admin admin = adminDao.getAdminByUser(user);
        admin.getUser().setSurname(editRequest.getSurname());
        admin.getUser().setName(editRequest.getName());
        admin.getUser().setPatronymic(editRequest.getPatronymic());
        admin.getUser().setPassword(editRequest.getNewPassword());
        admin.setPosition(editRequest.getPosition());
        int countUpdatedAdmins = adminDao.update(admin);
        if (countUpdatedAdmins == 0)
            throw new OptimisticLockException("Не удалось обновить данные админа, попробуйте снова!");
        return AdminMapStruct.INSTANCE.fromAdminToEditingAdminDtoResponse(admin);
    }

    @Transactional
    public List<BusDtoResponse> getBuses(String cookie) throws ServerException {
        ServiceUtils.getAdminUserByCookie(cookie, sessionDao);

        List<Bus> buses = adminDao.getAllBuses();
        List<BusDtoResponse> busDtoResponses = new ArrayList<>();
        for (Bus bus : buses) {
            BusDtoResponse busDtoResponse = BusMapStruct.INSTANCE.fromBusToBusDtoResponse(bus);
            busDtoResponses.add(busDtoResponse);
        }
        return busDtoResponses;
    }

}

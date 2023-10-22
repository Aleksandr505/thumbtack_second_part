package com.gladun.buscompany.controller;

import com.gladun.buscompany.dto.request.EditingAdminDtoRequest;
import com.gladun.buscompany.dto.request.RegisterAdminDtoRequest;
import com.gladun.buscompany.dto.response.AdminDtoResponse;
import com.gladun.buscompany.dto.response.BusDtoResponse;
import com.gladun.buscompany.dto.response.ClientDtoResponse;
import com.gladun.buscompany.dto.response.EditingAdminDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.Client;
import com.gladun.buscompany.service.AdminService;
import com.gladun.buscompany.service.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AdminDtoResponse register(@Valid @RequestBody RegisterAdminDtoRequest registerAdminDtoRequest, HttpServletResponse httpServletResponse) throws ServerException {
        return adminService.register(registerAdminDtoRequest, httpServletResponse);
    }

    @GetMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ClientDtoResponse> getClients(@CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return adminService.getAllClients(cookieValue);
    }

    @PutMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EditingAdminDtoResponse editAdmin(@Valid @RequestBody EditingAdminDtoRequest editingAdminDtoRequest,
                                             @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException, OptimisticLockException {
        return adminService.updateAdmin(editingAdminDtoRequest, cookieValue);
    }

    @GetMapping(value = "/buses", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<BusDtoResponse> getBuses(@CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return adminService.getBuses(cookieValue);
    }

}

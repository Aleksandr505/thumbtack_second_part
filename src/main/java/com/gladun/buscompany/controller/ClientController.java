package com.gladun.buscompany.controller;

import com.gladun.buscompany.dto.request.EditingAdminDtoRequest;
import com.gladun.buscompany.dto.request.EditingClientDtoRequest;
import com.gladun.buscompany.dto.request.RegisterClientDtoRequest;
import com.gladun.buscompany.dto.response.ClientDtoResponse;
import com.gladun.buscompany.dto.response.EditingClientDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.service.ClientService;
import com.gladun.buscompany.service.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ClientDtoResponse register(@Valid @RequestBody RegisterClientDtoRequest registerClientDtoRequest, HttpServletResponse httpServletResponse) throws ServerException {
        return clientService.register(registerClientDtoRequest, httpServletResponse);
    }

    @PutMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EditingClientDtoResponse editClient(@Valid @RequestBody EditingClientDtoRequest editingClientDtoRequest,
                                               @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException, OptimisticLockException {
        return clientService.updateClient(editingClientDtoRequest, cookieValue);
    }

}

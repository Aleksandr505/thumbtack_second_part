package com.gladun.buscompany.controller;

import com.gladun.buscompany.dto.response.UserDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.service.ServiceUtils;
import com.gladun.buscompany.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserController {

    private final SessionService sessionService;

    @Autowired
    public UserController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDtoResponse getUserData(@CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return sessionService.getUserData(cookieValue);
    }

    @DeleteMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteUser(@CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        sessionService.deleteUser(cookieValue);
    }
}

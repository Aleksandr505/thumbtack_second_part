package com.gladun.buscompany.controller;

import com.gladun.buscompany.dto.request.LoginDtoRequest;
import com.gladun.buscompany.dto.response.UserDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.service.ServiceUtils;
import com.gladun.buscompany.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDtoResponse login(@Valid @RequestBody LoginDtoRequest loginDtoRequest, HttpServletResponse httpServletResponse) throws ServerException {
        return sessionService.login(loginDtoRequest, httpServletResponse);
    }

    @DeleteMapping(value = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void logout(@CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        sessionService.logout(cookieValue);
    }

}

package com.gladun.buscompany.service;

import com.gladun.buscompany.dao.SessionDao;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.model.RoleEnum;
import com.gladun.buscompany.model.Session;
import com.gladun.buscompany.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServiceUtils {

    public final static String COOKIE_NAME = "JAVASESSIONID";

    public static void addCookie(HttpServletResponse httpServletResponse, Session session) {
        Cookie cookie = new Cookie(COOKIE_NAME, session.getCookie());
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        httpServletResponse.addCookie(cookie);
    }

    public static User getAdminUserByCookie(String cookie, SessionDao sessionDao) throws ServerException {
        User user = sessionDao.getUserByCookie(cookie);
        if (user == null)
            throw new ServerException(ServerErrorCode.WRONG_COOKIE);
        if (user.getUserType() != RoleEnum.ADMIN)
            throw new ServerException(ServerErrorCode.ACCESS_ERROR);
        return user;
    }

    public static User getClientUserByCookie(String cookie, SessionDao sessionDao) throws ServerException {
        User user = sessionDao.getUserByCookie(cookie);
        if (user == null)
            throw new ServerException(ServerErrorCode.WRONG_COOKIE);
        if (user.getUserType() != RoleEnum.CLIENT)
            throw new ServerException(ServerErrorCode.ACCESS_ERROR);
        return user;
    }

    /*public static String extractCookie(HttpServletRequest req) {
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals("JAVASESSIONID"))
                return c.getValue();
        }
        return null;
    }*/

}

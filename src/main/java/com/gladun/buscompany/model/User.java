package com.gladun.buscompany.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class User {

    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private String login;
    private String password;
    private RoleEnum userType;
    private Long version;

    public User(int id, String surname, String name, String patronymic, String login, String password, RoleEnum userType, Long version) {
        this(surname, name, patronymic, login, password, userType, version);
        this.id = id;
    }

    public User(String surname, String name, String patronymic, String login, String password, RoleEnum userType, Long version) {
        this.id = 0;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
        this.userType = userType;
        this.version = version;
    }
}

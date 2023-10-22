package com.gladun.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private User user;
    private String email;
    private String phone;
    private List<Order> orders;
    // REVU может, сюда List<Order>
    // и тогда при получении клиента можно LAZY получить все его Order
}

package com.gladun.buscompany.controller;

import com.gladun.buscompany.dto.request.OrderDtoRequest;
import com.gladun.buscompany.dto.response.OrderDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.service.OrderService;
import com.gladun.buscompany.service.ServiceUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderDtoResponse addOrder(@Valid @RequestBody OrderDtoRequest orderDtoRequest, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException, OptimisticLockException {
        return orderService.addOrder(orderDtoRequest, cookieValue);
    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderDtoResponse> getOrdersByParams(@RequestParam(value = "fromStation", required = false) String fromStation,
                                                    @RequestParam(value = "toStation", required = false) String toStation,
                                                    @RequestParam(value = "busName", required = false) String busName,
                                                    @RequestParam(value = "fromDate", required = false) String fromDate,
                                                    @RequestParam(value = "toDate", required = false) String toDate,
                                                    @RequestParam(value = "clientId", required = false) String clientId,
                                                    @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        return orderService.getOrdersByParams(fromStation, toStation, busName, fromDate, toDate, clientId, cookieValue);
    }

    @DeleteMapping(value = "/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void cancelOrder(@PathVariable int orderId, @CookieValue(ServiceUtils.COOKIE_NAME) String cookieValue) throws ServerException {
        orderService.cancelOrder(orderId, cookieValue);
    }

}

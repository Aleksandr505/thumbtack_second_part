package com.gladun.buscompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dto.PassengerDto;
import com.gladun.buscompany.dto.request.*;
import com.gladun.buscompany.dto.response.OrderDtoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderTests extends TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    public void addOrderTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
        createClient(client);

        LoginDtoRequest loginRequest1 = new LoginDtoRequest(client.getLogin(), client.getPassword());
        Cookie cookie1 = loginUser(loginRequest1);

        List<PassengerDto> passengers = new ArrayList<>();
        passengers.add(new PassengerDto("Александр", "Пушкин", "4533-55551-2255"));
        passengers.add(new PassengerDto("Михаил", "Ломоносов", "1487-93751-2968"));
        OrderDtoRequest orderDtoRequest = new OrderDtoRequest(tripId, LocalDate.now().plusDays(8).toString(), passengers);

        mvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(cookie1)
                    .content(objectMapper.writeValueAsString(orderDtoRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void getOrdersByParamsTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
        createClient(client);

        LoginDtoRequest loginRequest1 = new LoginDtoRequest(client.getLogin(), client.getPassword());
        Cookie cookie1 = loginUser(loginRequest1);

        List<PassengerDto> passengers1 = new ArrayList<>();
        passengers1.add(new PassengerDto("Александр", "Пушкин", "4533-55551-2255"));
        passengers1.add(new PassengerDto("Михаил", "Ломоносов", "1487-93751-2968"));
        OrderDtoRequest orderDtoRequest1 = new OrderDtoRequest(tripId, LocalDate.now().plusDays(8).toString(), passengers1);

        List<PassengerDto> passengers2 = new ArrayList<>();
        passengers2.add(new PassengerDto("Лев", "Толстой", "9678-3128-4698"));
        passengers2.add(new PassengerDto("Фёдор", "Достоевский", "2164216-52-455"));
        OrderDtoRequest orderDtoRequest2 = new OrderDtoRequest(tripId, LocalDate.now().plusDays(15).toString(), passengers2);

        createOrder(orderDtoRequest1, cookie1);
        createOrder(orderDtoRequest2, cookie1);

        mvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie1)
                        .param("busName", "Mercedes"))
                .andExpect(status().isOk());
    }

    @Test
    public void cancelOrderTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
        createClient(client);

        LoginDtoRequest loginRequest1 = new LoginDtoRequest(client.getLogin(), client.getPassword());
        Cookie cookie1 = loginUser(loginRequest1);

        List<PassengerDto> passengers = new ArrayList<>();
        passengers.add(new PassengerDto("Александр", "Пушкин", "4533-55551-2255"));
        passengers.add(new PassengerDto("Михаил", "Ломоносов", "1487-93751-2968"));
        OrderDtoRequest orderDtoRequest = new OrderDtoRequest(tripId, LocalDate.now().plusDays(8).toString(), passengers);

        OrderDtoResponse orderResponse = createOrder(orderDtoRequest, cookie1);

        mvc.perform(delete("/api/orders/" + orderResponse.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie1))
                .andExpect(status().isOk());
    }

}

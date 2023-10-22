package com.gladun.buscompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dto.PassengerDto;
import com.gladun.buscompany.dto.request.*;
import com.gladun.buscompany.dto.response.OrderDtoResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OptimisticBlockingTests extends TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimisticBlockingTests.class);

    @Test
    public void optimisticUpdateUserTest() throws Exception {
        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
        createClient(client);

        LoginDtoRequest loginRequest = new LoginDtoRequest(client.getLogin(), client.getPassword());
        Cookie cookie = loginUser(loginRequest);

        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                LOGGER.info("Thread " + finalI + " start!");
                try {
                    EditingClientDtoRequest dtoRequest = new EditingClientDtoRequest("Смирнов", "Алексей",
                            "Павлович", "CorrectEmailTest@gmail.com", "+7 904 850-18-79",
                            client.getPassword(), "645NGDfJHFBJHB4PAS" + finalI);

                    mvc.perform(put("/api/clients")
                                    .cookie(cookie)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(dtoRequest)));
                } catch (Exception e) {
                    LOGGER.info("Exception in Thread " + finalI);
                }
                LOGGER.info("Thread " + finalI + " final!");
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    public void optimisticInsertOrderTest() throws Exception {
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

        Thread[] threads = new Thread[50];
        for (int i = 0; i < 50; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                LOGGER.info("Thread " + finalI + " start!");
                try {
                    List<PassengerDto> passengers = new ArrayList<>();
                    passengers.add(new PassengerDto("Александр", "Пушкин", "4533-55551-2255-" + finalI));
                    passengers.add(new PassengerDto("Михаил", "Ломоносов", "1487-93751-2968-" + finalI));
                    OrderDtoRequest orderDtoRequest = new OrderDtoRequest(tripId, LocalDate.now().plusDays(8).toString(), passengers);

                    mvc.perform(post("/api/orders")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .cookie(cookie1)
                                    .content(objectMapper.writeValueAsString(orderDtoRequest)))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    LOGGER.info("Exception in Thread " + finalI);
                }
                LOGGER.info("Thread " + finalI + " final!");
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    public void optimisticUpdatePlaceTest() throws Exception {
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
        PassengerDto passengerDto = passengers.get(0);

        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                LOGGER.info("Thread " + finalI + " start!");
                try {
                    ChoicePlaceDtoRequest request = new ChoicePlaceDtoRequest(orderResponse.getOrderId(), passengerDto.getLastName(),
                            passengerDto.getFirstName(), passengerDto.getPassport(), 5);

                    mvc.perform(post("/api/places")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .cookie(cookie1)
                                    .content(objectMapper.writeValueAsString(request)));
                } catch (Exception e) {
                    LOGGER.info("Exception in Thread " + finalI);
                }
                LOGGER.info("Thread " + finalI + " final!");
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

}

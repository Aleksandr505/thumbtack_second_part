package com.gladun.buscompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dto.PassengerDto;
import com.gladun.buscompany.dto.request.*;
import com.gladun.buscompany.dto.response.OrderDtoResponse;
import com.gladun.buscompany.service.PlaceService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlaceTests extends TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceTests.class);

    @Test
    public void choicePlaceTest() throws Exception {
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

        ChoicePlaceDtoRequest request = new ChoicePlaceDtoRequest(orderResponse.getOrderId(), passengerDto.getLastName(),
                passengerDto.getFirstName(), passengerDto.getPassport(), 5);
        LOGGER.info("Test ChoicePlaceDtoRequest: {}", request);

        mvc.perform(post("/api/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie1)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAvailablePlacesTest() throws Exception {
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

        PassengerDto passengerDto1 = passengers.get(0);
        ChoicePlaceDtoRequest request1 = new ChoicePlaceDtoRequest(orderResponse.getOrderId(), passengerDto1.getLastName(),
                passengerDto1.getFirstName(), passengerDto1.getPassport(), 5);
        PassengerDto passengerDto2 = passengers.get(1);
        ChoicePlaceDtoRequest request2 = new ChoicePlaceDtoRequest(orderResponse.getOrderId(), passengerDto2.getLastName(),
                passengerDto2.getFirstName(), passengerDto2.getPassport(), 10);

        choicePlace(request1, cookie1);
        choicePlace(request2, cookie1);

        mvc.perform(get("/api/places/" + orderResponse.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie1))
                .andExpect(status().isOk());
    }

}

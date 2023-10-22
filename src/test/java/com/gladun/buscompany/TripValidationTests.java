package com.gladun.buscompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dto.request.LoginDtoRequest;
import com.gladun.buscompany.dto.request.RegisterAdminDtoRequest;
import com.gladun.buscompany.dto.request.RegisterClientDtoRequest;
import com.gladun.buscompany.dto.request.TripDtoRequest;
import com.gladun.buscompany.exception.ServerErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TripValidationTests extends TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    public void cookieValidationTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] dates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, dates);

        Cookie wrongCookie = new Cookie(cookie.getName(), cookie.getValue() + "WRONG_VALUE");

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(wrongCookie))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ServerErrorCode.WRONG_COOKIE.getMessage()));
    }

    @Test
    public void accessValidationTest() throws Exception {
        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
        createClient(client);

        LoginDtoRequest loginRequest = new LoginDtoRequest(client.getLogin(), client.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] dates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, dates);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ServerErrorCode.ACCESS_ERROR.getMessage()));
    }

    @Test
    public void addTripValidationTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        TripDtoRequest tripDtoRequest = new TripDtoRequest("", "Centre", null,
                "24:01", -60, "50 долларов", null, null);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editTripValidationTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        String[] dates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest tripDtoRequest = new TripDtoRequest("WrongBusName", "Periphery", "Centre",
                "11:30", Duration.ofHours(1).toMinutes(), "8000 руб.", null, dates);

        mvc.perform(put("/api/trips/" + tripId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ServerErrorCode.BUS_NOT_FOUND.getMessage()));
    }

    @Test
    public void getTripValidationTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        int wrongId = tripId + 5000;

        mvc.perform(get("/api/trips/" + wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ServerErrorCode.TRIP_NOT_FOUND.getMessage()));
    }

    @Test
    public void approveTripTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        int wrongId = tripId + 5000;

        mvc.perform(put("/api/trips/" + wrongId + "/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ServerErrorCode.TRIP_NOT_FOUND.getMessage()));
    }

}

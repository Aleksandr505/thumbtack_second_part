package com.gladun.buscompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dto.request.LoginDtoRequest;
import com.gladun.buscompany.dto.request.RegisterAdminDtoRequest;
import com.gladun.buscompany.dto.request.RegisterClientDtoRequest;
import com.gladun.buscompany.dto.request.TripDtoRequest;
import com.gladun.buscompany.dto.response.TripDtoResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TripTests extends TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    private static final Logger LOGGER = LoggerFactory.getLogger(TripTests.class);

    @Test
    public void addTripTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] dates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, dates);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void editTripTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        String[] dates = {LocalDate.now().plusDays(30).toString(), LocalDate.now().plusDays(36).toString(), LocalDate.now().plusDays(45).toString()};
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Hyundai", "Periphery", "Centre",
                "11:30", Duration.ofHours(1).toMinutes(), "8000 руб.", null, dates);

        mvc.perform(put("/api/trips/" + tripId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTripTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        mvc.perform(delete("/api/trips/" + tripId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void getTripTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        int tripId = createTrip(trip, cookie).getTripId();

        mvc.perform(get("/api/trips/" + tripId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk());
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

        mvc.perform(put("/api/trips/" + tripId + "/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void getTripsByParams1Test() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        createTrip(trip, cookie);

        String[] baseDates1 = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip1 = new TripDtoRequest("Hyundai", "Centre", "Periphery",
                "08:00", Duration.ofHours(5).toMinutes(), "5000 руб.", null, baseDates1);
        TripDtoResponse response = createTrip(trip1, cookie);
        LOGGER.info("TripDtoResponse: {}", response);

        mvc.perform(get("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .queryParam("busName", "Hyundai"))
                        .andExpect(status().isOk());
    }

    @Test
    public void getTripsByParams2Test() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        createTrip(trip, cookie);

        String[] baseDates1 = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip1 = new TripDtoRequest("Hyundai", "Centre", "Periphery",
                "08:00", Duration.ofHours(5).toMinutes(), "5000 руб.", null, baseDates1);
        createTrip(trip1, cookie);

        mvc.perform(get("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .queryParam("fromStation", "Centre"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTripsByParams3Test() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        createTrip(trip, cookie);

        String[] baseDates1 = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip1 = new TripDtoRequest("Hyundai", "Centre", "Periphery",
                "08:00", Duration.ofHours(5).toMinutes(), "5000 руб.", null, baseDates1);
        createTrip(trip1, cookie);

        mvc.perform(get("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .queryParam("fromDate", "2022-01-08"))
                .andExpect(status().isOk());
    }


    @Test
    public void getTripsByParams4Test() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        createTrip(trip, cookie);

        String[] baseDates1 = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip1 = new TripDtoRequest("Hyundai", "Centre", "Periphery",
                "08:00", Duration.ofHours(5).toMinutes(), "5000 руб.", null, baseDates1);
        createTrip(trip1, cookie);

        mvc.perform(get("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .queryParam("toDate", "2022-11-01"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTripsByParams5Test() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        createTrip(trip, cookie);

        String[] baseDates1 = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip1 = new TripDtoRequest("Hyundai", "Centre", "Periphery",
                "08:00", Duration.ofHours(5).toMinutes(), "5000 руб.", null, baseDates1);
        createTrip(trip1, cookie);

        mvc.perform(get("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void getTripsByParams6Test() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
        createClient(client);

        LoginDtoRequest loginRequest1 = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie1 = loginUser(loginRequest1);

        LoginDtoRequest loginRequest2 = new LoginDtoRequest(client.getLogin(), client.getPassword());
        Cookie cookie2 = loginUser(loginRequest2);

        String[] baseDates = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, baseDates);
        createTrip(trip, cookie1);

        String[] baseDates1 = {LocalDate.now().plusDays(5).toString(), LocalDate.now().plusDays(8).toString(), LocalDate.now().plusDays(15).toString()};
        TripDtoRequest trip1 = new TripDtoRequest("Hyundai", "Centre", "Periphery",
                "08:00", Duration.ofHours(5).toMinutes(), "5000 руб.", null, baseDates1);
        int tripId = createTrip(trip1, cookie1).getTripId();

        approveTrip(tripId, cookie1);

        mvc.perform(get("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie2)
                        .queryParam("fromStation", "Centre"))
                .andExpect(status().isOk());
    }

}

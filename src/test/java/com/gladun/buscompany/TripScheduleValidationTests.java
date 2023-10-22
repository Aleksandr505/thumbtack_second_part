package com.gladun.buscompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dto.request.LoginDtoRequest;
import com.gladun.buscompany.dto.request.RegisterAdminDtoRequest;
import com.gladun.buscompany.dto.request.TripDtoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TripScheduleValidationTests extends TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    public void addTripWithScheduleDailyTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        TripDtoRequest.ScheduleDto scheduleDto = new TripDtoRequest.ScheduleDto(LocalDate.now().toString(),
                LocalDate.now().plusDays(10).toString(), "daily");
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", scheduleDto, null);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void addTripWithScheduleOddTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        TripDtoRequest.ScheduleDto scheduleDto = new TripDtoRequest.ScheduleDto(LocalDate.now().toString(),
                LocalDate.now().plusDays(10).toString(), "odd");
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", scheduleDto, null);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void addTripWithScheduleEvenTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        TripDtoRequest.ScheduleDto scheduleDto = new TripDtoRequest.ScheduleDto(LocalDate.now().toString(),
                LocalDate.now().plusDays(10).toString(), "even");
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", scheduleDto, null);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void addTripWithScheduleDayOfWeekTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        TripDtoRequest.ScheduleDto scheduleDto = new TripDtoRequest.ScheduleDto(LocalDate.now().toString(),
                LocalDate.now().plusDays(10).toString(), "Sun,Tue,Sat");
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", scheduleDto, null);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void addTripWithScheduleDayOfMonthTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        TripDtoRequest.ScheduleDto scheduleDto = new TripDtoRequest.ScheduleDto(LocalDate.now().toString(),
                LocalDate.now().plusDays(10).toString(), "2,9,15,21,28,31");
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", scheduleDto, null);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void addTripWithScheduleWrongTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        TripDtoRequest.ScheduleDto scheduleDto = new TripDtoRequest.ScheduleDto(LocalDate.now().toString(),
                LocalDate.now().plusDays(10).toString(), "Wrong period, 233354");
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", scheduleDto, null);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTripWithDatesTest() throws Exception {
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
    public void addTripWithDatesWrongTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        String[] dates = {"222-48-05", "sdjfhsdgh"};
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", null, dates);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTripWithScheduleWrongValidationTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        TripDtoRequest.ScheduleDto scheduleDto = new TripDtoRequest.ScheduleDto("",
                "2020-23654-8896fgf", null);
        TripDtoRequest tripDtoRequest = new TripDtoRequest("Mercedes", "Centre", "Periphery",
                "11:00", Duration.ofHours(3).toMinutes(), "4000 руб.", scheduleDto, null);

        mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDtoRequest))
                        .cookie(cookie))
                .andExpect(status().isBadRequest());
    }

}

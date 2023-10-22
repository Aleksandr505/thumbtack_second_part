package com.gladun.buscompany;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dao.*;
import com.gladun.buscompany.dto.request.*;
import com.gladun.buscompany.dto.response.ChoicePlaceDtoResponse;
import com.gladun.buscompany.dto.response.OrderDtoResponse;
import com.gladun.buscompany.dto.response.TripDtoResponse;
import com.gladun.buscompany.exception.ServerException;
import com.gladun.buscompany.mappers.TripMapper;
import com.gladun.buscompany.model.Bus;
import com.gladun.buscompany.model.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Autowired
    protected AdminDao adminDao;
    @Autowired
    protected ClientDao clientDao;
    @Autowired
    protected TripDao tripDao;
    @Autowired
    protected DebugDao debugDao;
    @Autowired
    protected SessionDao sessionDao;

    @Autowired
    protected TripMapper tripMapper;

    @BeforeEach()
    public void clearDatabaseAndInsertDefaultBuses() throws ServerException {
        debugDao.clearDatabase();

        debugDao.insertBus(new Bus("Mercedes", 35));
        debugDao.insertBus(new Bus("Hyundai", 24));
        debugDao.insertBus(new Bus("ГАЗ", 18));
    }

    protected void createAdmin(RegisterAdminDtoRequest admin) throws Exception {
        mvc.perform(post("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JAVASESSIONID"))
                .andExpect(jsonPath("$.surname").value(admin.getSurname()))
                .andExpect(jsonPath("$.name").value(admin.getName()))
                .andExpect(jsonPath("$.patronymic").value(admin.getPatronymic()))
                .andExpect(jsonPath("$.position").value(admin.getPosition()))
                .andExpect(jsonPath("$.userType").value(RoleEnum.ADMIN.toString()));
    }

    protected void createClient(RegisterClientDtoRequest client) throws Exception {
        mvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JAVASESSIONID"))
                .andExpect(jsonPath("$.surname").value(client.getSurname()))
                .andExpect(jsonPath("$.name").value(client.getName()))
                .andExpect(jsonPath("$.patronymic").value(client.getPatronymic()))
                .andExpect(jsonPath("$.email").value(client.getEmail()))
                .andExpect(jsonPath("$.phone").value(client.getPhone().replaceAll("-", "")))
                .andExpect(jsonPath("$.userType").value(RoleEnum.CLIENT.toString()));
    }

    protected TripDtoResponse createTrip(TripDtoRequest trip, Cookie cookie) throws Exception {
        ResultActions resultActions = mvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trip))
                        .cookie(cookie))
                .andExpect(status().isOk());

        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TripDtoResponse.class);
    }

    protected OrderDtoResponse createOrder(OrderDtoRequest order, Cookie cookie) throws Exception {
        ResultActions resultActions = mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), OrderDtoResponse.class);
    }

    protected void approveTrip(int tripId, Cookie cookie) throws Exception {
        mvc.perform(put("/api/trips/" + tripId + "/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    protected Cookie loginUser(LoginDtoRequest loginRequest) throws Exception {
        return mvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JAVASESSIONID"))
                .andReturn().getResponse().getCookie("JAVASESSIONID");
    }

    protected ChoicePlaceDtoResponse choicePlace(ChoicePlaceDtoRequest request, Cookie cookie) throws Exception {
        ResultActions resultActions = mvc.perform(post("/api/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ChoicePlaceDtoResponse.class);
    }

    /*void configureMessageConverters(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }*/
}

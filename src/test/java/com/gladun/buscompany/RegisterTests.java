package com.gladun.buscompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dto.request.RegisterAdminDtoRequest;
import com.gladun.buscompany.dto.request.RegisterClientDtoRequest;
import com.gladun.buscompany.exception.ServerErrorCode;
import com.gladun.buscompany.model.RoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegisterTests extends TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    public void registerAdminTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
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

    @Test
    public void registerClientTest() throws Exception {
        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
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

    @Test
    public void registerClientWithErrorsTest() throws Exception {
        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Smirnov", "", "Павлович",
                "Micha584", "56gSE45684ds1", "Mic", "+++79064646867");
        mvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerBusyAdminTest() throws Exception {
        createAdmin(new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher"));

        RegisterAdminDtoRequest request = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        mvc.perform(post("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ServerErrorCode.LOGIN_BUSY.getMessage()));
    }

}

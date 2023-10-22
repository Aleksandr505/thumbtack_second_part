package com.gladun.buscompany;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladun.buscompany.dto.request.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetUserInformationTests extends TestBase {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    public void getUserDataTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        mvc.perform(get("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void getClientsTest() throws Exception {
        RegisterClientDtoRequest client1 = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
        RegisterClientDtoRequest client2 = new RegisterClientDtoRequest("вапврвр", "вапварпвр", "впвавмвав",
                "jHHGGhhjsd", "5d5f1g5df1gdf5d", "sdgdfgsf@gmail.com", "+7 904 850-18-79");
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createClient(client1);
        createClient(client2);
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        mvc.perform(get("/api/clients")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void editAdminTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        EditingAdminDtoRequest dtoRequest = new EditingAdminDtoRequest("Гладун", "Иван",
                "Викторович", "Higher", admin.getPassword(), "545FGfgs56652D");

        mvc.perform(put("/api/admins")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void editClientTest() throws Exception {
        RegisterClientDtoRequest client = new RegisterClientDtoRequest("Смирнов", "Михаил", "Павлович",
                "Micha584", "56gSE457d8fg", "Michail256@gmail.com", "+7 904 850-18-79");
        createClient(client);

        LoginDtoRequest loginRequest = new LoginDtoRequest(client.getLogin(), client.getPassword());
        Cookie cookie = loginUser(loginRequest);

        EditingClientDtoRequest dtoRequest = new EditingClientDtoRequest("Смирнов", "Алексей",
                "Павлович", "CorrectEmailTest@gmail.com", "+7 904 850-18-79",
                client.getPassword(), "645NGDfJHFBJHBJ56465");

        mvc.perform(put("/api/clients")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void getBusesTest() throws Exception {
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        createAdmin(admin);

        LoginDtoRequest loginRequest = new LoginDtoRequest(admin.getLogin(), admin.getPassword());
        Cookie cookie = loginUser(loginRequest);

        mvc.perform(get("/api/buses")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}

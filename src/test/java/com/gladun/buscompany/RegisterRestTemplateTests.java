package com.gladun.buscompany;

import com.gladun.buscompany.dto.request.RegisterAdminDtoRequest;
import com.gladun.buscompany.dto.response.AdminDtoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegisterRestTemplateTests extends TestBase {

    private static final String resourceUrl = "http://localhost:8888";

    @Test
    public void registerAdminTest() {
        RestTemplate restTemplate = new RestTemplate();
        RegisterAdminDtoRequest admin = new RegisterAdminDtoRequest("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "Higher");
        ResponseEntity<AdminDtoResponse> response = restTemplate.postForEntity(resourceUrl + "/api/admins", admin, AdminDtoResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getSurname(), admin.getSurname());
    }

    @Test
    public void getAllClientsInformationTest() throws Exception {
        /*Admin admin = insertAdmin(new User("Гладун", "Александр", null,
                "AleksGladun200", "fsfa545sd", "admin"), "Higher");
        Client client1 = insertClient(new User("Смирнов", "Михаил", "Павлович", "Micha584",
                "56gSE457d8fg", "client"), "Michail256@gmail.com", "+7 904 850-18-79", null);
        Client client2 = insertClient(new User("вапврвр", "вапварпвр", "впвавмвав", "jHHGGhhjsd",
                "5d5f1g5df1gdf5d", "client"), "sdgdfgsf@gmail.com", "+7 904 850-18-79", null);
        Cookie cookie = loginUser(admin.getUser());*/

        /*ClientHttpRequestFactory requestFactory = new
                HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(cookie.getName(), cookie.getValue());
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        assertTrue(Objects.requireNonNull(requestHeaders.getContentType()).includes(MediaType.APPLICATION_JSON));

        //configureMessageConverters(restTemplate);

        HttpEntity<UserDtoResponse[]> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<UserDtoResponse[]> responseEntity = restTemplate.exchange(resourceUrl + "/api/clients",
                HttpMethod.GET, requestEntity, UserDtoResponse[].class);
        UserDtoResponse[] userDtoResponse = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
*/
    }

}

package com.example.restapi;


import com.example.restapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class RestApiApplication extends SpringBootServletInitializer {

    private static RestTemplate restTemplate;

    @Autowired
    public RestApiApplication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseEntity<List<User>> responseEntity =
                    restTemplate.exchange("http://91.241.64.178:7081/api/users", HttpMethod.GET,
                            null, new ParameterizedTypeReference<List<User>>() {
                            });//Запрос к Rest API на получение списка всех пользователей
            HttpHeaders httpHeaders = responseEntity.getHeaders();
            String setCookie = httpHeaders.getFirst(httpHeaders.SET_COOKIE);
            HttpHeaders headers = new HttpHeaders();
            ResponseEntity<List<User>> allUsers = responseEntity;
            System.out.println(allUsers.getBody());//список всех пользователей

            headers.set("Cookie", setCookie);//куки из ответа на запрос

            User user = new User(3L, "James", "Brown", (byte) 23);//добавление пользователя с ID = 3
            String jsonString = mapper.writeValueAsString(user);
            headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
            // конвертация объекта пользователя в формат JSON
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestBody = new HttpEntity<>(jsonString, headers);
            ResponseEntity<String> responseEntityAddUser = restTemplate
                    .exchange("http://91.241.64.178:7081/api/users", HttpMethod.POST, requestBody, String.class);
            //Update пользователя c ID = 3
            user = new User(3L, "Thomas", "Shelby", (byte) 24);
            jsonString = mapper.writeValueAsString(user);
            requestBody = new HttpEntity<>(jsonString, headers);
            ResponseEntity<String> responseEntityUpdateUser = restTemplate
                    .exchange("http://91.241.64.178:7081/api/users", HttpMethod.PUT, requestBody, String.class);
            //Удаление пользователя с ID = 3
            ResponseEntity<String> responseEntityDeleteUser = restTemplate
                    .exchange("http://91.241.64.178:7081/api/users/3", HttpMethod.DELETE, requestBody, String.class);
            //Вывод результирующего кода
            System.out.println(responseEntityAddUser.getBody() + responseEntityUpdateUser.getBody()
                    + responseEntityDeleteUser.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

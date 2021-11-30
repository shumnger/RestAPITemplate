package com.example.restapi.controller;

import com.example.restapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class UserRestController {

    private static RestTemplate restTemplate;

    private static HttpHeaders headers;

    @Autowired
    public UserRestController(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
    }

    ObjectMapper mapper = new ObjectMapper();
    String jsonString = null;
    HttpEntity<String> requestBody = null;

    public ResponseEntity <List<User>> getAllUsers() {
        ResponseEntity<List<User>> allUsers =
                restTemplate.exchange("http://91.241.64.178:7081/api/users", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<User>>() {
                        });
        return allUsers;
    }

    public void saveUser(User user) throws Exception {
        jsonString = mapper.writeValueAsString(user);
        requestBody = new HttpEntity<>(jsonString, headers);
        ResponseEntity<String> responseEntityAddUser = restTemplate
                .exchange("http://91.241.64.178:7081/api/users", HttpMethod.POST, requestBody, String.class);
        System.out.println(responseEntityAddUser.getBody());
    }

    public void updateUser(User user) throws Exception {
        jsonString = mapper.writeValueAsString(user);
        requestBody = new HttpEntity<>(jsonString, headers);
        ResponseEntity<String> responseEntityUpdateUser = restTemplate
                .exchange("http://91.241.64.178:7081/api/users", HttpMethod.PUT, requestBody, String.class);
        System.out.println(responseEntityUpdateUser.getBody());
    }

    public void deleteUser(User user) throws Exception {
        jsonString = mapper.writeValueAsString(user);
        requestBody = new HttpEntity<>(jsonString, headers);
        ResponseEntity<String> responseEntityDeleteUser = restTemplate
                .exchange("http://91.241.64.178:7081/api/users/" + user.getId(), HttpMethod.DELETE, requestBody, String.class);
        System.out.println(responseEntityDeleteUser.getBody());
    }

}
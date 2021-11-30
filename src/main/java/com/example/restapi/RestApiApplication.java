package com.example.restapi;

import com.example.restapi.controller.UserRestController;
import com.example.restapi.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestApiApplication extends SpringBootServletInitializer {

    private static RestTemplate restTemplate;
    private static HttpHeaders headers;

    @Autowired
    public RestApiApplication(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
        UserRestController controller = new UserRestController(restTemplate, headers);
        try {
            System.out.println(controller.getAllUsers().getBody());
            String setCookie = controller.getAllUsers().getHeaders()
                    .getFirst(controller.getAllUsers().getHeaders().SET_COOKIE);
            headers.set("Cookie", setCookie);
            controller.saveUser(new User(3L, "James", "Brown", (byte) 23));
            controller.updateUser(new User(3L, "Thomas", "Shelby", (byte) 24));
            controller.deleteUser(new User(3L, "Thomas", "Shelby", (byte) 24));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

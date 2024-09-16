package com.example.server.example;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

@RestController
public class ExampleController {

    private HashMap<String, Integer> counterMap = new HashMap<>();

    @GetMapping("/example")
    public Object getExample(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        var id = UUID.randomUUID().toString();

        var cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-id")) {
                    id = cookie.getValue();
                }
            }
        }
        httpServletResponse.addCookie(new Cookie("user-id", id));

        var counter = counterMap.getOrDefault(id, 0);
        counter++;
        counterMap.put(id, counter);

        var response = new HashMap<>();
        response.put("message", "Hello, World!");
        response.put("counter", counter);
        return response;
    }

    @GetMapping("/example/session")
    public Object getExampleSession(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        var session = httpServletRequest.getSession();
        var counterString = session.getAttribute("counter");
        var counter = counterString == null ? 0 : (int) counterString;
        counter++;
        session.setAttribute("counter", counter);

        var response = new HashMap<>();
        response.put("message", "Hello, World!");
        response.put("counter", counter);
        return response;
    }
}


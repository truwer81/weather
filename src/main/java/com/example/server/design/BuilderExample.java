package com.example.server.design;

public class BuilderExample {

    public static void main(String[] args) {
        Person person = Person.builder()
                .name("John")
                .country(1.0f)
                .age(20)
                .build();
    }
}

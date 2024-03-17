package com.example.server.design;

public class BuilderExample {

    public static void main(String[] args) {
        Person person = Person.builder()
                .name("John")
                .address(Address.builder()
                        .street("Freedom")
                        .city("New York")
                        .zipCode("12345")
                        .build())
                .age(20)
                .build();
    }
}

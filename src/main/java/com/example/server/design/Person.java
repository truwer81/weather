package com.example.server.design;

// https://refactoring.guru/design-patterns/builder

import lombok.Getter;
import lombok.Setter;

// @Builder
public class Person {

    private String name;
    private Integer age;
    private Float country;
    private Address address;

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public static class PersonBuilder {
        private String name;
        private Integer age;
        private Address address;


        public PersonBuilder() {
        }


        public PersonBuilder name(String name) {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            this.name = name;
            return this;
        }


        public PersonBuilder age(Integer age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Invalid age");
            }
            this.age = age;
            return this;
        }

        public PersonBuilder address(Address address) {
            this.address = address;
            return this;
        }

        public Person build() {
            Person person = new Person();
            person.name = this.name;
            person.age = this.age;
            person.address = this.address;
            return person;
        }
    }


}
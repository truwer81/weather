package com.example.server.design;

// https://refactoring.guru/design-patterns/builder

// @Builder
public class Person {

    private String name;
    private Integer age;
    private Float country;

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public static class PersonBuilder {
        private String name;
        private Integer age;
        private Float country;

        public PersonBuilder() {
        }

        public PersonBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public PersonBuilder country(Float country) {
            this.country = country;
            return this;
        }

        public Person build() {
            Person person = new Person();
            person.name = this.name;
            person.age = this.age;
            person.country = this.country;
            return person;
        }
    }
}

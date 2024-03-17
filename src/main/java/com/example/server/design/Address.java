package com.example.server.design;

import lombok.Getter;
import lombok.Setter;

public class Address {
    private String city;
    private String street;
    private String zipCode;
    private String country;


    public static AddressBuilder builder() {
        return new AddressBuilder();
    }

    @Getter
    @Setter
    public static class AddressBuilder {
        private String city;
        private String street;
        private String zipCode;

        public AddressBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder street(String street) {
            this.street = street;
            return this;
        }

        public AddressBuilder zipCode(String zipCode) {
            if (!zipCode.matches("\\d{5}")) {
                throw new IllegalArgumentException("Invalid ZIP code");
            }
            this.zipCode = zipCode;
            return this;
        }

        public Address build() {
            Address address = new Address();
            address.city = this.city;
            address.street = this.street;
            address.zipCode = this.zipCode;
            return address;
        }
    }
}

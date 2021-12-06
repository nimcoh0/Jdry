package com.baeldung.jersey.server.model;

import org.softauto.annotations.DefaultValue;
import org.softauto.annotations.ExposedForTesting;
import org.softauto.annotations.RPC;

public class Person {
    String name;
    String address;

    @RPC
    @ExposedForTesting
    public Person(@DefaultValue("Abh") String name, @DefaultValue("Nepal") String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person [name: " + getName() + " address: " + getAddress() + "]";
    }
}
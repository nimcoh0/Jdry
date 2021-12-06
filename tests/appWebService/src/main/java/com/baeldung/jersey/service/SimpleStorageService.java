package com.baeldung.jersey.service;

import java.util.HashMap;
import java.util.Map;

import com.baeldung.jersey.server.model.Fruit;
import org.softauto.annotations.ListenerForTesting;
import org.softauto.annotations.RPC;

public class SimpleStorageService {

    private static final Map<String, Fruit> fruits = new HashMap<String, Fruit>();

    @RPC
    @ListenerForTesting
    public static void storeFruit(final Fruit fruit) {
        fruits.put(fruit.getName(), fruit);
    }

    @RPC
    @ListenerForTesting
    public static Fruit findByName(final String name) {
        return fruits.entrySet()
            .stream()
            .filter(map -> name.equals(map.getKey()))
            .map(map -> map.getValue())
            .findFirst()
            .get();
    }

}

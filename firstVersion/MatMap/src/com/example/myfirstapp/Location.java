package com.example.myfirstapp;

import java.util.HashMap;

/**
 * Created by mrshu on 12/12/14.
 */
public class Location {
    private String name;
    private HashMap<String, Double> aps = new HashMap<>();

    Location(String name) {
        this.name = name;
    }

    Location() {

    }

    Location setName(String name) {
        this.name = name;
        return this;
    }

    Location addAP(String ssid, Double signal) {
        this.aps.put(ssid.toLowerCase(), signal);
        return this;
    }

    Double getAPSignal(String ap) {
        return this.aps.get(ap);
    }

    String getName() {
        return this.name;
    }
}

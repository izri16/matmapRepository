package com.example.myfirstapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by mrshu on 12/12/14.
 */
public class Locator {
    final Double PROBAB_ZERO = 0.001;
    final Integer S_MAX = 100;
    private ArrayList<Location> locations = new ArrayList<>();
    Locator Locator() {
        return this;
    }

    Location addLocation() {
        Location location = new Location();
        this.locations.add(location);
        return location;
    }

    String localize(List<WifiInfo> scan) {
        String max_loc = "";
        Double max_prob = 1.0;

        for(Location loc: this.locations) {
            Double prob = 1.0;

            for(WifiInfo wifi: scan) {
                Double signal = loc.getAPSignal(wifi.getBSSID().toLowerCase());
                if (signal == null) {
                    prob *= PROBAB_ZERO;
                } else {
                    prob *= this.p(signal, wifi.getStrength());
                }
            }
            Log.d("as", prob + " " + loc.getName());
            if (prob > max_prob) {
                max_loc = loc.getName();
                max_prob = prob;
            }
        }

        return max_loc;
    }

    Double p(Double Li, Integer fi) {
        return S_MAX - Math.abs(Li - fi);
    }
}



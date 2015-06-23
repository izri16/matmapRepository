package com.example.matmap;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mrshu on 12/12/14.
 */
public class Locator {
    final Double PROBAB_ZERO = 0.001;
    final Double S_MAX = 100.0;
    private ArrayList<Location> locations = new ArrayList<>();
    Locator Locator() {
        return this;
    }

    Location addLocation() {
        Location location = new Location();
        this.locations.add(location);
        return location;
    }

    Location addLocation(Location l) {
        this.locations.add(l);
        return l;
    }

    String localize(List<WifiInfo> scan) {
        String max_loc = "";
        Double max_prob = 1.0;
        Log.d("localize", "init");
        for(Location loc: this.locations) {
            Double prob = 1.0;
            Log.d("localize", "location: " + loc.getName());
            for(WifiInfo wifi: scan) {
                Double signal = loc.getAPSignal(wifi.getBSSID().toLowerCase());
                Double strength = Locator.dBmToQuality(wifi.getStrength());

                Log.d("localize", "prob=" + prob);

                if (signal == null) {
                    prob *= PROBAB_ZERO;
                } else {
                    prob *= this.p(signal, strength);
                }
            }
            Log.d("localize", "computed probability: " + prob + " " + loc.getName());
            if (prob > max_prob) {
                max_loc = loc.getName();
                max_prob = prob;
            }
        }
        Log.d("localize", "after");

        return max_loc;
    }

    public static Double dBmToQuality(Integer dBm) {
        if (dBm <= -100) {
            return new Double(0.0);
        } else if (dBm >= -50) {
            return new Double(100.0);
        } else {
            return new Double(2 * (dBm + 100));
        }
    }

    public static Double dBmToQuality(Double dBm) {
        if (dBm <= -100) {
            return new Double(0.0);
        } else if (dBm >= -50) {
            return new Double(100.0);
        } else {
            return new Double(2 * (dBm + 100));
        }
    }

    Double p(Double Li, Double fi) {
        return S_MAX - Math.abs(Li - fi);
    }
}



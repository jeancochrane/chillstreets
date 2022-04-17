package com.chillstreets.routing;

import com.graphhopper.routing.profiles.EncodedValue;
import com.graphhopper.routing.util.BikeFlagEncoder;
import com.graphhopper.util.PMap;

import java.util.List;

public class ChillstreetsFlagEncoder extends CarFlagEncoder {

    public ChillstreetsFlagEncoder() {
        this(5, 5, 0);
    }

    public ChillstreetsFlagEncoder(int speedBits, double speedFactor, int maxTurnCosts) {
        super(speedBits, speedFactor, maxTurnCosts);
    }

    public ChillstreetsFlagEncoder(PMap properties) {
        super(properties);
    }

    @Override
    public void createEncodedValues(List<EncodedValue> registerNewEncodedValue, String prefix, int index) {
        // first two bits are reserved for route handling in superclass
        super.createEncodedValues(registerNewEncodedValue, prefix, index);
    }

    @Override
    public String toString() {
        return "chillstreets";
    }
}
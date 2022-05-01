package com.chillstreets.routing;

import com.chillstreets.routing.ChillstreetsTagParser;

import com.graphhopper.routing.util.FlagEncoderFactory;
import com.graphhopper.routing.util.FlagEncoders;
import com.graphhopper.util.PMap;

public class ChillstreetsFlagEncoderFactory implements FlagEncoderFactory {
    final String CHILLSTREETS = "chillstreets";

    @Override
    public FlagEncoder createFlagEncoder(String name, PMap configuration) {
        if (name.equals(ROADS))
            return FlagEncoders.createRoads();

        if (name.equals(CAR))
            return FlagEncoders.createCar(configuration);

        if (name.equals(CAR4WD))
            return FlagEncoders.createCar4wd(configuration);

        if (name.equals(BIKE))
            return FlagEncoders.createBike(configuration);

        if (name.equals(BIKE2))
            return FlagEncoders.createBike2(configuration);

        if (name.equals(RACINGBIKE))
            return FlagEncoders.createRacingBike(configuration);

        if (name.equals(MOUNTAINBIKE))
            return FlagEncoders.createMountainBike(configuration);

        if (name.equals(FOOT))
            return FlagEncoders.createFoot(configuration);

        if (name.equals(HIKE))
            return FlagEncoders.createHike(configuration);

        if (name.equals(MOTORCYCLE))
            return FlagEncoders.createMotorcycle(configuration);

        if (name.equals(WHEELCHAIR))
            return FlagEncoders.createWheelchair(configuration);

        if (name.equals(CHILLSTREETS))
            return new ChillstreetsTagParser(configuration);

        throw new IllegalArgumentException("entry in encoder list not supported " + name);
    }
}
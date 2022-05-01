package com.chillstreets.routing;

import com.graphhopper.routing.util.BikeTagParser;

public class ChillstreetsTagParser extends BikeTagParser {
    public ChillstreetsTagParser() {
        this("chillstreets");
    }

    public ChillstreetsTagParser(String name) {
        super(name, 4, 2, 0, false);
    }

    public ChillstreetsTagParser(PMap properties) {
        this(properties.getString("name", "chillstreets"),
                properties.getInt("speed_bits", 4),
                properties.getInt("speed_factor", 2),
                properties.getInt("max_turn_costs", properties.getBool("turn_costs", false) ? 1 : 0),
                properties.getBool("speed_two_directions", false));

        blockPrivate(properties.getBool("block_private", true));
        blockFords(properties.getBool("block_fords", false));
    }

    public ChillstreetsTagParser(int speedBits, double speedFactor, int maxTurnCosts, boolean speedTwoDirections) {
        super("chillstreets", speedBits, speedFactor, maxTurnCosts, speedTwoDirections);
    }
}

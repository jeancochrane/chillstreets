package com.chillstreets.routing;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.routing.weighting.FastestWeighting;


public class ChillstreetsWeighting extends FastestWeighting {
    // TODO: Override methods on this class to define weighting
    @Override
    public String getName() {
        return "chillstreets";
    }
}
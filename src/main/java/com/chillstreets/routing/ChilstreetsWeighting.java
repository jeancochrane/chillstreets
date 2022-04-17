package com.chillstreets.routing;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.routing.weighting.ShortestWeighting;


public class ChillstreetsWeighting extends ShortestWeighting {
    public ChillstreetsWeighting(FlagEncoder flagEncoder) {
        super(flagEncoder);
    }

    @Override
    public double getMinWeight(double currDistToGoal) {
        return currDistToGoal;
    }

    @Override
    public double calcWeight(EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId) {
        return edgeState.getDistance();
    }

    @Override
    public String getName() {
        return "chillstreets_shortest";
    }
}
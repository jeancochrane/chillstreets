package com.chillstreets;

import com.chillstreets.routing.ChillstreetsFlagEncoderFactory;
import com.chillstreets.routing.ChillstreetsWeightingFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.Graph;

public class Chillstreets extends GraphHopper {
    @Override
    protected WeightingFactory createWeightingFactory() {
        return new ChillstreetsWeightingFactory(ghStorage.getBaseGraph(), getEncodingManager());
    }
}
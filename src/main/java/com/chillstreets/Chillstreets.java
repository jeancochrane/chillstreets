package com.chillstreets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graphhopper.json.geo.JsonFeatureCollection;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.Graph;
import com.chillstreets.routing.ChillstreetsFlagEncoderFactory;
import com.chillstreets.routing.ChillstreetsWeighting;

public class Chillstreets extends GraphHopperOSM {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Chillstreets(JsonFeatureCollection landmarkSplittingFeatureCollection) {
        super(landmarkSplittingFeatureCollection);
        logger.info("Running Chillstreets");

        super.setFlagEncoderFactory(new ChillstreetsFlagEncoderFactory());
    }

    @Override
    public Weighting createWeighting(HintsMap hintsMap, FlagEncoder encoder, Graph graph) {
        String weightingStr = hintsMap.getWeighting().toLowerCase();
        if ("chillstreets_shortest".equals(weightingStr)) {
            return new ChillstreetsWeighting(encoder);
        } else {
            return super.createWeighting(hintsMap, encoder, graph);
        }
    }
}
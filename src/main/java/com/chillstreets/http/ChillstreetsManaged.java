package com.chillstreets.http;

import com.chillstreets.routing.ChillstreetsFlagEncoderFactory;

import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopperConfig;
import main.java.com.chillstreets.Chillstreets;

import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChillstreetsManaged implements Managed {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GraphHopper graphHopper;

    public ChillstreetsManaged(GraphHopperConfig configuration) {
        graphHopper = new Chillstreets();
        graphHopper.setFlagEncoderFactory(new ChillstreetsFlagEncoderFactory());
        graphHopper.init(configuration);
    }

    @Override
    public void start() {
        graphHopper.importOrLoad();
        logger.info("loaded graph at:{}, data_reader_file:{}, encoded values:{}, {} ints for edge flags, {}",
                graphHopper.getGraphHopperLocation(), graphHopper.getOSMFile(),
                graphHopper.getEncodingManager().toEncodedValuesAsString(),
                graphHopper.getEncodingManager().getIntsForFlags(),
                graphHopper.getGraphHopperStorage().toDetailsString());
    }

    public GraphHopper getGraphHopper() {
        return graphHopper;
    }

    @Override
    public void stop() {
        graphHopper.close();
    }
}
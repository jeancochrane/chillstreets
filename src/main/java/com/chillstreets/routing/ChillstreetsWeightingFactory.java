package main.java.com.chillstreets.routing;

import com.chillstreets.routing.ChillstreetsWeighting;

import com.graphhopper.util.PMap;
import com.graphhopper.routing.DefaultWeightingFactory;

import static com.graphhopper.util.Helper.toLowerCase;


public class ChillstreetsWeightingFactory extends DefaultWeightingFactory {
    @Override
    public Weighting createWeighting(Profile profile, PMap requestHints, boolean disableTurnCosts) {
        PMap hints = new PMap();
        hints.putAll(profile.getHints());
        hints.putAll(requestHints);

        String weightingStr = toLowerCase(profile.getWeighting());
        if (weightingStr.isEmpty())
            throw new IllegalArgumentException("You have to specify a weighting");

        if ("chillstreets_shortest".equals(weightingStr)) {
            return new ChillstreetsWeighting(encoder, hints);
        } else {
            return super.createWeighting(profile, requestHints, disableTurnCosts);
        }
    }
}

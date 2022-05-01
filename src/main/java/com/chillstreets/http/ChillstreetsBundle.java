package com.chillstreets.http;

import com.chillstreets.http.ChillstreetsManaged;

import com.graphhopper.GraphHopper;
import com.graphhopper.http.GraphHopperBundle;
import com.graphhopper.http.GraphHopperBundleConfiguration;
import com.graphhopper.http.IllegalArgumentExceptionMapper;
import com.graphhopper.http.health.GraphHopperHealthCheck;
import com.graphhopper.isochrone.algorithm.JTSTriangulator;
import com.graphhopper.routing.ProfileResolver;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.util.TranslationMap;
import com.graphhopper.util.details.PathDetailsBuilderFactory;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ChillstreetsBundle extends GraphHopperBundle {

    @Override
    public void run(GraphHopperBundleConfiguration configuration, Environment environment) {
        for (Object k : System.getProperties().keySet()) {
            if (k instanceof String && ((String) k).startsWith("graphhopper."))
                throw new IllegalArgumentException("You need to prefix system parameters with '-Ddw.graphhopper.' instead of '-Dgraphhopper.' see #1879 and #1897");
        }

        // When Dropwizard's Hibernate Validation misvalidates a query parameter,
        // a JerseyViolationException is thrown.
        // With this mapper, we use our custom format for that (backwards compatibility),
        // and also coerce the media type of the response to JSON, so we can return JSON error
        // messages from methods that normally have a different return type.
        // That's questionable, but on the other hand, Dropwizard itself does the same thing,
        // not here, but in a different place (the custom parameter parsers).
        // So for the moment we have to assume that both mechanisms
        // a) always return JSON error messages, and
        // b) there's no need to annotate the method with media type JSON for that.
        //
        // However, for places that throw IllegalArgumentException or MultiException,
        // we DO need to use the media type JSON annotation, because
        // those are agnostic to the media type (could be GPX!), so the server needs to know
        // that a JSON error response is supported. (See below.)
        environment.jersey().register(new GHJerseyViolationExceptionMapper());

        // If the "?type=gpx" parameter is present, sets a corresponding media type header
        environment.jersey().register(new TypeGPXFilter());

        // Together, these two take care that MultiExceptions thrown from RouteResource
        // come out as JSON or GPX, depending on the media type
        environment.jersey().register(new MultiExceptionMapper());
        environment.jersey().register(new MultiExceptionGPXMessageBodyWriter());

        // This makes an IllegalArgumentException come out as a MultiException with
        // a single entry.
        environment.jersey().register(new IllegalArgumentExceptionMapper());

        final ChillstreetsManaged graphHopperManaged = new ChillstreetsManaged(configuration.getGraphHopperConfiguration());
        environment.lifecycle().manage(graphHopperManaged);

        final GraphHopper graphHopper = graphHopperManaged.getGraphHopper();
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(configuration.getGraphHopperConfiguration()).to(GraphHopperConfig.class);
                bind(graphHopper).to(GraphHopper.class);

                bind(new JTSTriangulator(graphHopper.getRouterConfig())).to(Triangulator.class);
                bindFactory(PathDetailsBuilderFactoryFactory.class).to(PathDetailsBuilderFactory.class);
                bindFactory(ProfileResolverFactory.class).to(ProfileResolver.class);
                bindFactory(HasElevation.class).to(Boolean.class).named("hasElevation");
                bindFactory(LocationIndexFactory.class).to(LocationIndex.class);
                bindFactory(TranslationMapFactory.class).to(TranslationMap.class);
                bindFactory(EncodingManagerFactory.class).to(EncodingManager.class);
                bindFactory(GraphHopperStorageFactory.class).to(GraphHopperStorage.class);
                bindFactory(GtfsStorageFactory.class).to(GtfsStorage.class);
            }
        });

        environment.jersey().register(MVTResource.class);
        environment.jersey().register(NearestResource.class);
        environment.jersey().register(RouteResource.class);
        environment.jersey().register(IsochroneResource.class);
        environment.jersey().register(MapMatchingResource.class);
        if (configuration.getGraphHopperConfiguration().has("gtfs.file")) {
            // These are pt-specific implementations of /route and /isochrone, but the same API.
            // We serve them under different paths (/route-pt and /isochrone-pt), and forward
            // requests for ?vehicle=pt there.
            environment.jersey().register(new AbstractBinder() {
                @Override
                protected void configure() {
                    if (configuration.getGraphHopperConfiguration().getBool("gtfs.free_walk", false)) {
                        bind(PtRouterFreeWalkImpl.class).to(PtRouter.class);
                    } else {
                        bind(PtRouterImpl.class).to(PtRouter.class);
                    }
                }
            });
            environment.jersey().register(PtRouteResource.class);
            environment.jersey().register(PtIsochroneResource.class);
            environment.jersey().register(PtMVTResource.class);
            environment.jersey().register(PtRedirectFilter.class);
        }
        environment.jersey().register(SPTResource.class);
        environment.jersey().register(I18NResource.class);
        environment.jersey().register(InfoResource.class);
        environment.healthChecks().register("graphhopper", new GraphHopperHealthCheck(graphHopper));
        environment.jersey().register(environment.healthChecks());
        environment.jersey().register(HealthCheckResource.class);
    }
}
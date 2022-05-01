package com.chillstreets.http;

import com.chillstreets.http.ChillstreetsBundle;

import com.graphhopper.application.cli.ImportCommand;
import com.graphhopper.application.cli.MatchCommand;
import com.graphhopper.application.resources.RootResource;
import com.graphhopper.http.CORSFilter;
import com.graphhopper.http.RealtimeBundle;
import com.graphhopper.navigation.NavigateResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public final class ChillstreetsApplication extends Application<GraphHopperServerConfiguration> {

    public static void main(String[] args) throws Exception {
        new ChillstreetsApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<GraphHopperServerConfiguration> bootstrap) {
        bootstrap.addBundle(new ChillstreetsBundle());
        bootstrap.addBundle(new RealtimeBundle());
        bootstrap.addCommand(new ImportCommand());
        bootstrap.addCommand(new MatchCommand());
        bootstrap.addBundle(new AssetsBundle("/com/graphhopper/maps/", "/maps/", "index.html"));
        // see this link even though its outdated?! // https://www.webjars.org/documentation#dropwizard
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources/webjars", "/webjars/", null, "webjars"));
    }

    @Override
    public void run(GraphHopperServerConfiguration configuration, Environment environment) {
        environment.jersey().register(new RootResource());
        environment.jersey().register(NavigateResource.class);
        environment.servlets().addFilter("cors", CORSFilter.class).addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "*");
    }

}
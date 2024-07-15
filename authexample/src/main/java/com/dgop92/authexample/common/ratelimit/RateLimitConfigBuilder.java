package com.dgop92.authexample.common.ratelimit;

import io.github.bucket4j.Bandwidth;

import java.util.Map;

public class RateLimitConfigBuilder {

    public interface RateLimitConfigBuilderRouteStage {
        RateLimitConfigBuilderMethodStage route(String route);
    }

    public interface RateLimitConfigBuilderMethodStage {

        RateLimitConfigBuilderMethodStage postMethod(Bandwidth bandwidth);

        RateLimitConfigBuilderMethodStage getMethod(Bandwidth bandwidth);

        RateLimitConfigBuilderMethodStage putMethod(Bandwidth bandwidth);

        RateLimitConfigBuilderMethodStage patchMethod(Bandwidth bandwidth);

        RateLimitConfigBuilderMethodStage deleteMethod(Bandwidth bandwidth);

        RateLimitConfigBuilderRouteStage and();

        Map<String, RouteRateLimitConfig> build();
    }

    public static RateLimitConfigBuilderRouteStage builder() {
        return new RateLimitConfigBuilderImpl();
    }

    private final static class RateLimitConfigBuilderImpl
            implements RateLimitConfigBuilderRouteStage, RateLimitConfigBuilderMethodStage {

        private final Map<String, RouteRateLimitConfig> rateLimitConfig;

        private String currentRoute;

        public RateLimitConfigBuilderImpl() {
            this.rateLimitConfig = new java.util.HashMap<>();
        }

        @Override
        public RateLimitConfigBuilderMethodStage route(String route) {
            this.currentRoute = route;
            this.rateLimitConfig.put(route, new RouteRateLimitConfig());
            return this;
        }

        @Override
        public RateLimitConfigBuilderMethodStage postMethod(Bandwidth bandwidth) {
            this.rateLimitConfig.get(currentRoute).setPostMethodBandwidth(bandwidth);
            return this;
        }

        @Override
        public RateLimitConfigBuilderMethodStage getMethod(Bandwidth bandwidth) {
            this.rateLimitConfig.get(currentRoute).setGetMethodBandwidth(bandwidth);
            return this;
        }

        @Override
        public RateLimitConfigBuilderMethodStage putMethod(Bandwidth bandwidth) {
            this.rateLimitConfig.get(currentRoute).setPutMethodBandwidth(bandwidth);
            return this;
        }

        @Override
        public RateLimitConfigBuilderMethodStage patchMethod(Bandwidth bandwidth) {
            this.rateLimitConfig.get(currentRoute).setPatchMethodBandwidth(bandwidth);
            return this;
        }

        @Override
        public RateLimitConfigBuilderMethodStage deleteMethod(Bandwidth bandwidth) {
            this.rateLimitConfig.get(currentRoute).setDeleteMethodBandwidth(bandwidth);
            return this;
        }

        @Override
        public RateLimitConfigBuilderRouteStage and() {
            return this;
        }

        @Override
        public Map<String, RouteRateLimitConfig> build() {
            return this.rateLimitConfig;
        }
    }
}

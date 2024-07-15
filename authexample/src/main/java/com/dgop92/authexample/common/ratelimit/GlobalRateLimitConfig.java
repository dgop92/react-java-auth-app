package com.dgop92.authexample.common.ratelimit;

import com.dgop92.authexample.path.ControllerPaths;
import io.github.bucket4j.Bandwidth;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class GlobalRateLimitConfig {

    private final Map<String, RouteRateLimitConfig> publicBucketConfigPerRoute;

    private final Map<String, RouteRateLimitConfig> privateBucketConfigPerRoute;

    public GlobalRateLimitConfig() {
        this.publicBucketConfigPerRoute = RateLimitConfigBuilder.builder()
                .route(String.format("%s%s", ControllerPaths.API_V1_USERS, "/create-email-password"))
                .postMethod(Bandwidth.builder().capacity(8).refillIntervally(8, Duration.ofMinutes(1)).build())
                .build();
        this.privateBucketConfigPerRoute = RateLimitConfigBuilder.builder()
                .route(String.format("%s%s", ControllerPaths.API_V1_USERS, "/me"))
                .getMethod(Bandwidth.builder().capacity(50).refillIntervally(10, Duration.ofMinutes(1)).build())
                .patchMethod(Bandwidth.builder().capacity(10).refillIntervally(10, Duration.ofMinutes(1)).build())
                .deleteMethod(Bandwidth.builder().capacity(10).refillIntervally(10, Duration.ofMinutes(1)).build())
                .build();
    }

    public Map<String, RouteRateLimitConfig> getPrivateBucketConfigPerRoute() {
        return privateBucketConfigPerRoute;
    }

    public Map<String, RouteRateLimitConfig> getPublicBucketConfigPerRoute() {
        return publicBucketConfigPerRoute;
    }
}

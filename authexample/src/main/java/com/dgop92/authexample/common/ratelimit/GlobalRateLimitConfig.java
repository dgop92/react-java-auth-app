package com.dgop92.authexample.common.ratelimit;

import java.util.Map;

public interface GlobalRateLimitConfig {

    Map<String, RouteRateLimitConfig> getPrivateBucketConfigPerRoute();

    Map<String, RouteRateLimitConfig> getPublicBucketConfigPerRoute();
}

package com.dgop92.authexample.common.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnonymousThrottlingFilter extends GenericThrottlingFilter {

    Logger logger = LoggerFactory.getLogger(AnonymousThrottlingFilter.class);

    private final Map<String, RouteRateLimitConfig> bucketConfigPerRoute;

    private final Map<String, Bucket> buckets;

    public AnonymousThrottlingFilter(
            Map<String, RouteRateLimitConfig> bucketConfigPerRoute
    ) {
        this.bucketConfigPerRoute = bucketConfigPerRoute;
        buckets = new ConcurrentHashMap<>();
    }

    @Override
    protected Bucket getBucket(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String route = request.getRequestURI();
        String methodName = request.getMethod();
        String key = String.format("th-%s-%s-%s", ip, route, methodName);

        Bucket bucket = buckets.get(key);
        if (bucket == null) {
            logger.info("Creating new bucket for key: {}", key);
            RouteRateLimitConfig routeRateLimitConfig = bucketConfigPerRoute.get(route);
            if (routeRateLimitConfig == null) {
                throw new IllegalStateException("No http method config found for route: " + route);
            }
            Bandwidth bandwidth = routeRateLimitConfig.getMethodBandwidth(methodName);
            bucket = Bucket.builder().addLimit(bandwidth).build();
            buckets.put(key, bucket);
        }

        return bucket;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        Set<String> publicRoutes = this.bucketConfigPerRoute.keySet();
        // should not filter if route is not in public routes
        boolean shouldNotFilter = !publicRoutes.contains(request.getRequestURI());

        if (shouldNotFilter) {
            logger.info("Request to route: {} is not in public routes, ignoring", request.getRequestURI());
        }

        return shouldNotFilter;
    }
}

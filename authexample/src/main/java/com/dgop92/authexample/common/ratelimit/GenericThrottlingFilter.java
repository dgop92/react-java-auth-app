package com.dgop92.authexample.common.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GenericThrottlingFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(GenericThrottlingFilter.class);

    private final Map<String, RouteRateLimitConfig> bucketConfigPerRoute;

    private final Map<String, Bucket> buckets;

    protected GenericThrottlingFilter(Map<String, RouteRateLimitConfig> bucketConfigPerRoute) {
        this.bucketConfigPerRoute = bucketConfigPerRoute;
        this.buckets = new ConcurrentHashMap<>();
    }

    protected abstract String getTrackerKey(HttpServletRequest request);
    protected String getTypeOfFilterName() {
        return "";
    }

    protected Bucket getBucket(HttpServletRequest request) {
        String key = getTrackerKey(request);

        String route = request.getRequestURI();
        String methodName = request.getMethod();

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
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Bucket bucket = getBucket(request);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // the limit is not exceeded
            String remainingRequests = Long.toString(probe.getRemainingTokens());
            response.addHeader("X-Rate-Limit-Remaining", remainingRequests);
            filterChain.doFilter(request, response);
        } else {
            // limit is exceeded
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

            response.addHeader("X-Rate-Limit-Retry-After-Seconds", Long.toString(waitForRefill));
            response.setContentType("text/plain");
            response.setStatus(429);
            response.getWriter().append("Too many requests");
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        Set<String> routes = this.bucketConfigPerRoute.keySet();
        // should not filter if route is not in the configured routes
        boolean shouldNotFilter = !routes.contains(request.getRequestURI());
        if (shouldNotFilter) {
            logger.info("Request to route: {} is not in the configured {} routes, ignoring", request.getRequestURI(), getTypeOfFilterName() );
        }

        return shouldNotFilter;
    }
}

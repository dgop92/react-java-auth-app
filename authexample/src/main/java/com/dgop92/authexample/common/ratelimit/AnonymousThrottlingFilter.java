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

    public AnonymousThrottlingFilter(Map<String, RouteRateLimitConfig> bucketConfigPerRoute) {
        super(bucketConfigPerRoute);
    }

    @Override
    protected String getTypeOfFilterName() {
        return "public";
    }

    @Override
    protected String getTrackerKey(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String route = request.getRequestURI();
        String methodName = request.getMethod();
        return String.format("th-%s-%s-%s", ip, route, methodName);
    }
}

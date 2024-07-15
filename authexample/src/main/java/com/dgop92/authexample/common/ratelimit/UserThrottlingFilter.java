package com.dgop92.authexample.common.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class UserThrottlingFilter extends GenericThrottlingFilter {

    public UserThrottlingFilter(Map<String, RouteRateLimitConfig> bucketConfigPerRoute) {
        super(bucketConfigPerRoute);
    }

    @Override
    protected String getTypeOfFilterName() {
        return "private";
    }

    @Override
    protected String getTrackerKey(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        String route = request.getRequestURI();
        String methodName = request.getMethod();
        return String.format("th-%s-%s-%s", userId, route, methodName);
    }
}

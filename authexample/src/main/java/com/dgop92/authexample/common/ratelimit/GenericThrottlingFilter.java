package com.dgop92.authexample.common.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public abstract class GenericThrottlingFilter extends OncePerRequestFilter {

    protected abstract Bucket getBucket(HttpServletRequest request);

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
}

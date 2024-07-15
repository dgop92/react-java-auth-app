package com.dgop92.authexample.common;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends CommonsRequestLoggingFilter {

    public RequestLoggingFilter() {
        super.setIncludeQueryString(true);
        super.setIncludeHeaders(false);
        /*super.setIncludePayload(true);
        super.setMaxPayloadLength(10000);
        super.setAfterMessagePrefix("REQUEST DATA: ");*/
    }
}

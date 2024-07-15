package com.dgop92.authexample.common.ratelimit;


import io.github.bucket4j.Bandwidth;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RouteRateLimitConfig {

    private Bandwidth postMethodBandwidth;

    private Bandwidth getMethodBandwidth;

    private Bandwidth putMethodBandwidth;

    private Bandwidth patchMethodBandwidth;

    private Bandwidth deleteMethodBandwidth;


    public Bandwidth getMethodBandwidth(String method) {
        return switch (method) {
            case "POST" -> postMethodBandwidth;
            case "GET" -> getMethodBandwidth;
            case "PUT" -> putMethodBandwidth;
            case "PATCH" -> patchMethodBandwidth;
            case "DELETE" -> deleteMethodBandwidth;
            default -> null;
        };
    }
}

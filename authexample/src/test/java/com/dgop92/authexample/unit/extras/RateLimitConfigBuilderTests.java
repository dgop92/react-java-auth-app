package com.dgop92.authexample.unit.extras;

import com.dgop92.authexample.common.ratelimit.RateLimitConfigBuilder;
import io.github.bucket4j.Bandwidth;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class RateLimitConfigBuilderTests {

    @Test
    public void Should_CreateRateLimitConfig_WithOneRoute() {
        Bandwidth b1 = Bandwidth.builder().capacity(8).refillIntervally(8, Duration.ofMinutes(1)).build();

        var config = RateLimitConfigBuilder.builder()
                .route("/route1")
                .postMethod(b1)
                .build();

        Assertions.assertThat(config.size()).isEqualTo(1);
        Assertions.assertThat(config.get("/route1").getPostMethodBandwidth()).isEqualTo(b1);
    }

    @Test
    public void Should_CreateRateLimitConfig_WithOneRouteAndAllMethods() {
        Bandwidth b1 = Bandwidth.builder().capacity(10).refillIntervally(10, Duration.ofMinutes(1)).build();
        Bandwidth b2 = Bandwidth.builder().capacity(11).refillIntervally(11, Duration.ofMinutes(1)).build();
        Bandwidth b3 = Bandwidth.builder().capacity(12).refillIntervally(12, Duration.ofMinutes(1)).build();
        Bandwidth b4 = Bandwidth.builder().capacity(13).refillIntervally(13, Duration.ofMinutes(1)).build();
        Bandwidth b5 = Bandwidth.builder().capacity(14).refillIntervally(14, Duration.ofMinutes(1)).build();

        var config = RateLimitConfigBuilder.builder()
                .route("/route1")
                .postMethod(b1)
                .getMethod(b2)
                .putMethod(b3)
                .patchMethod(b4)
                .deleteMethod(b5)
                .build();

        Assertions.assertThat(config.size()).isEqualTo(1);
        Assertions.assertThat(config.get("/route1").getPostMethodBandwidth()).isEqualTo(b1);
        Assertions.assertThat(config.get("/route1").getGetMethodBandwidth()).isEqualTo(b2);
        Assertions.assertThat(config.get("/route1").getPutMethodBandwidth()).isEqualTo(b3);
        Assertions.assertThat(config.get("/route1").getPatchMethodBandwidth()).isEqualTo(b4);
        Assertions.assertThat(config.get("/route1").getDeleteMethodBandwidth()).isEqualTo(b5);
    }

    @Test
    public void Should_CreateRateLimitConfig_WithTwoRoutes() {
        Bandwidth b1 = Bandwidth.builder().capacity(8).refillIntervally(8, Duration.ofMinutes(1)).build();
        Bandwidth b2 = Bandwidth.builder().capacity(9).refillIntervally(9, Duration.ofMinutes(1)).build();

        var config = RateLimitConfigBuilder.builder()
                .route("/route1")
                .postMethod(b1)
                .and()
                .route("/route2")
                .deleteMethod(b2)
                .build();

        Assertions.assertThat(config.size()).isEqualTo(2);
        Assertions.assertThat(config.get("/route1").getPostMethodBandwidth()).isEqualTo(b1);
        Assertions.assertThat(config.get("/route2").getDeleteMethodBandwidth()).isEqualTo(b2);
    }
}

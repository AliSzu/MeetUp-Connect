package meetup.connect.googleApi;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Map;

@Configuration
public class GoogleServiceCircuitBreaker {

    protected static final String GOOGLE_CALENDAR_CIRCUIT_BREAKER = "googleApiService";

    @Bean(GOOGLE_CALENDAR_CIRCUIT_BREAKER)
    public CircuitBreaker googleApiServiceConfig() {
        CircuitBreakerConfig googleApiServiceConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowSize(2)
                .recordExceptions(GeneralSecurityException.class, IOException.class)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =  CircuitBreakerRegistry.of(
                Map.of(GOOGLE_CALENDAR_CIRCUIT_BREAKER, googleApiServiceConfig)
        );

        return circuitBreakerRegistry.circuitBreaker(GOOGLE_CALENDAR_CIRCUIT_BREAKER);
    }
}



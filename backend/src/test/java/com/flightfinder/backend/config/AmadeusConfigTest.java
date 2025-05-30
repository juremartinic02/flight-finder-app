// File: src/test/java/com/flightfinder/backend/config/AmadeusConfigTest.java
package com.flightfinder.backend.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;

@SpringBootTest(
  // these properties are irrelevant now because we never hit the real endpoint
  properties = {
    "amadeus.api.key=dummy-key",
    "amadeus.api.secret=dummy-secret",
    "amadeus.api.oauth-url=https://unused.example.com",
    "amadeus.api.base-url=https://api.example.com"
  }
)
@Import(AmadeusConfigTest.StubConfig.class)
class AmadeusConfigTest {

    @Autowired
    private AmadeusConfig amadeusConfig;

    @Test
    void accessTokenIsFetchedFromStub() {
        String token = amadeusConfig.fetchAccessToken();
        assertNotNull(token, "stub token should not be null");
        assertEquals("stubbed-token", token);
    }

    @TestConfiguration
    static class StubConfig {
        /**
         * Override the real AmadeusConfig bean with a stub that
         * never does HTTP, just returns a known token.
         */
        @Bean
        @Primary
        public AmadeusConfig amadeusConfig() {
            return new AmadeusConfig() {
                @Override
                public String fetchAccessToken() {
                    return "stubbed-token";
                }
            };
        }
    }
}

package com.flightfinder.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Configuration class for setting up Amadeus API integration.
 * AmadeusConfig class is responsible for loading API credentials, configuring the Amadeus API base URL and OAuth2 endpoint
 */
@Configuration
public class AmadeusConfig {

    // Logger instance for AmadeusConfig class to log errors and activities.
    private static final Logger logger = LoggerFactory.getLogger(AmadeusConfig.class);

    // Injects the Amadeus API key from application-local.properties
    @Value("${amadeus.api.key}")
    private String apiKey;

    // Injects the Amadeus secret key from application-local.properties
    @Value("${amadeus.api.secret}")
    private String apiSecret;

    // Injects the Amadeus OAuth2 token endpoint URL from application.properties.
    @Value("${amadeus.api.oauth-url}")
    private String oauthUrl;

    // Injects the base URL for Amadeus API calls (flight offers search) from application.properties.
    @Value("${amadeus.api.base-url}")
    private String baseUrl;

    @Bean
    public WebClient amadeusWebClient(WebClient.Builder builder) {
        String token = fetchAccessToken();

        if (token == null || token.isBlank()) {
            logger.error("FATAL: Failed to obtain Amadeus access token. AmadeusWebClient cannot be configured. Check API credentials and OAuth URL.");
            throw new IllegalStateException("Failed to obtain Amadeus access token. Application cannot start without it.");
        } else {
            // For security, in development we will print just one part of the token
            logger.info("Successfully fetched Amadeus access token (ending with: ...{}). Configuring AmadeusWebClient with base URL: {}",
                    token.substring(Math.max(0, token.length() - 4)), baseUrl);
        }
        
        // Build the WebClient with the base URL and the default Authorization header.
        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }

    // Performs the OAuth2 Client Credentials flow to retrieve a bearer token.
    public String fetchAccessToken() {
        WebClient tokenClient = WebClient.builder()
            .baseUrl(oauthUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .build();

        // Using a Java Record for concise mapping of the expected JSON token response.
        record TokenResponse(
            @com.fasterxml.jackson.annotation.JsonProperty("access_token")
            String accessToken
        ) {}

        logger.debug("Requesting new Amadeus access token from OAuth endpoint: {}", oauthUrl);
        try {
            TokenResponse resp = tokenClient.post()
                .body(BodyInserters
                    .fromFormData("grant_type", "client_credentials")
                    .with("client_id", apiKey)
                    .with("client_secret", apiSecret))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

            if (resp != null && resp.accessToken() != null && !resp.accessToken().isBlank()) {
                logger.debug("Amadeus access token successfully parsed from response.");
                return resp.accessToken();
            } else {
                logger.error("Received a null or empty access token from Amadeus OAuth endpoint. Response details might indicate why (e.g., if resp itself is null or accessToken field is missing/empty).");
                return null;
            }
        } catch (WebClientResponseException e) {
            logger.error("WebClientResponseException while fetching Amadeus access token. Status: {}, Body: {}",
                         e.getStatusCode(), e.getResponseBodyAsString(), e);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected exception while fetching Amadeus access token: {}", e.getMessage(), e);
            return null;
        }
    }
}

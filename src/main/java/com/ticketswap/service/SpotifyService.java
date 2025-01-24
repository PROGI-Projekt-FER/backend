package com.ticketswap.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketswap.dto.spotify.ArtistDetailsDto;
import com.ticketswap.dto.spotify.TokenDto;
import com.ticketswap.dto.spotify.TokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpotifyService {
    private static final String SPOTIFY_BASE_URL = "https://api.spotify.com/v1/";
    private static final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    private String accessToken;
    private Instant tokenExpiryTime;

    public SpotifyService() {
        this.restTemplate = new RestTemplate();
    }

    private void authenticate() {
        if (accessToken == null || Instant.now().isAfter(tokenExpiryTime.minusSeconds(60))) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " +
                    Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

            ResponseEntity<TokenResponseDto> response = restTemplate.exchange(
                    SPOTIFY_TOKEN_URL, HttpMethod.POST, request, TokenResponseDto.class);

            if (response.getBody() != null) {
                TokenDto tokenDto = TokenDto.fromResponse(response.getBody());
                this.accessToken = tokenDto.getAccessToken();

                Long expiresIn = tokenDto.getExpiresIn();
                this.tokenExpiryTime = Instant.now().plusSeconds(expiresIn);
            } else {
                throw new RuntimeException("Failed to authenticate with Spotify API");
            }
        }
    }

    public List<Map.Entry<String, String>> searchArtists(String query) {
        authenticate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = SPOTIFY_BASE_URL + "search?q=" + query + "&type=artist&limit=10";

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        if (response.getBody() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                JsonNode itemsNode = rootNode.path("artists").path("items");

                List<Map.Entry<String, String>> artistDetails = new ArrayList<>();
                if (itemsNode.isArray()) {
                    for (JsonNode item : itemsNode) {
                        String artistName = item.path("name").asText();
                        String artistId = item.path("id").asText();
                        artistDetails.add(Map.entry(artistName, artistId));
                    }
                }

                return artistDetails;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse Spotify search response", e);
            }
        }

        return List.of();
    }

    public ArtistDetailsDto getArtistDetails(String artistId) {
        authenticate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = SPOTIFY_BASE_URL + "artists/" + artistId;

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        if (response.getBody() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                ArtistDetailsDto artistDetails = null;

                artistDetails = ArtistDetailsDto.map(rootNode);

                return artistDetails;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse Spotify search response", e);
            }
        }

        throw new RuntimeException("Unable to fetch artist details");
    }
}
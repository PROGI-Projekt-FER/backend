package com.ticketswap.dto.spotify;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDetailsDto {
    private String name;
    private String imageUrl;
    private int followers;
    private List<String> genres;

    public static ArtistDetailsDto map(JsonNode rootNode) {
        String name = rootNode.path("name").asText();

        int followers = rootNode.path("followers").path("total").asInt();

        List<String> genres = new ArrayList<>();
        rootNode.path("genres").forEach(genreNode -> genres.add(genreNode.asText()));

        String imageUrl = null;
        JsonNode imagesNode = rootNode.path("images");
        if (imagesNode.isArray() && imagesNode.size() > 0) {
            imageUrl = imagesNode.get(0).path("url").asText();
        }

        return new ArtistDetailsDto(name, imageUrl, followers, genres);
    }
}

package com.ticketswap.dto.spotify;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String accessToken;
    private String tokenType;
    private long expiresIn;

    public static TokenDto fromResponse(TokenResponseDto tokenResponseDto) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(tokenResponseDto.getAccessToken());
        tokenDto.setTokenType(tokenResponseDto.getTokenType());
        tokenDto.setExpiresIn(tokenResponseDto.getExpiresIn());
        return tokenDto;
    }
}
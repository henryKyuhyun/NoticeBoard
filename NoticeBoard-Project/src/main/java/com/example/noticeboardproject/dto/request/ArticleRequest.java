package com.example.noticeboardproject.dto.request;

import com.example.noticeboardproject.dto.ArticleDto;
import com.example.noticeboardproject.dto.HashtagDto;
import com.example.noticeboardproject.dto.UserAccountDto;

import java.util.Set;

public record ArticleRequest(
        String title,
        String content
) {

    public static ArticleRequest of(String title, String content) {
        return new ArticleRequest(title, content);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return toDto(userAccountDto, null);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto, String hashtagDtos) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtagDtos
        );
    }

}
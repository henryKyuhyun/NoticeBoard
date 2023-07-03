package com.example.noticeboardproject.service;

import com.example.noticeboardproject.domain.type.SearchType;
import com.example.noticeboardproject.dto.ArticleDto;
import com.example.noticeboardproject.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true) // Searching 은 Reading 하는 거고 변경하는게 아니기떄문에 readOnly 값을 준다.
    public Page<ArticleDto> searchArticles(SearchType title, String searchKeyword) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticle(long l) {
        return null;
    }
}

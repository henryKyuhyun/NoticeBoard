package com.example.noticeboardproject.service;

import com.example.noticeboardproject.dto.ArticleCommentDto;
import com.example.noticeboardproject.repository.ArticleCommentRepository;
import com.example.noticeboardproject.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private ArticleRepository articleRepository;


    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComment(Long articleId) {
        return List.of();
    }
}

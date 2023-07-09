package com.example.noticeboardproject.service;

import com.example.noticeboardproject.domain.Article;
import com.example.noticeboardproject.dto.ArticleCommentDto;
import com.example.noticeboardproject.repository.ArticleCommentRepository;
import com.example.noticeboardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;


    @DisplayName("게시물 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsComments(){
//        Given
        Long articleId = 1L;

        BDDMockito.given(articleRepository.findById(articleId)).willReturn(Optional.of(Article.of("title","content","#java")));
//        When
        List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
//        Then
        assertThat(articleComments).isNotNull();
        then(articleRepository).should().findById(articleId);

    }

//    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
//    @Test
//    void givenArticleId_whenSearchingArticleComments_thenReturnsComments(){
////        Given
//        Long articleId = 1L;
//
//        BDDMockito.given(articleRepository.findById(articleId)).willReturn(Optional.of(Article.of("title","content","#java")));
////        When
//        List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
////        Then
//        assertThat(articleComments).isNotNull();
//        then(articleRepository).should().findById(articleId);
//
//    }

}
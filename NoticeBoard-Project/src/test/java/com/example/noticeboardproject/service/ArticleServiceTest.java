package com.example.noticeboardproject.service;

import com.example.noticeboardproject.domain.Article;
import com.example.noticeboardproject.domain.type.SearchType;
import com.example.noticeboardproject.dto.ArticleDto;
import com.example.noticeboardproject.dto.ArticleUpdateDto;
import com.example.noticeboardproject.repository.ArticleRepository;
import com.example.noticeboardproject.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//import static org.junit.jupiter.api.Assertions.*;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService sut;
    @Mock private ArticleRepository articleRepository;
    @Mock private UserAccountRepository userAccountRepository;


    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage(){
//        Given
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
//        When
        Page<ArticleDto> articles = sut.searchArticles(null,null,pageable);
//        Then
        assertThat(articles).isEmpty();
        BDDMockito.then(articleRepository).should().findAll(pageable);
    }


    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchingParameters_whenSearchingArticles_thenReturnsArticlePage(){
//        Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());
//        When
        Page<ArticleDto> articles = sut.searchArticles(searchType,searchKeyword,pageable);
//        Then
        assertThat(articles).isEmpty();
        BDDMockito.then(articleRepository).should().findByTitleContaining(searchKeyword,pageable);
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
   void givenNoSearchParameters_whenSearchingArticlesViaHastag_thenReturnsEmptyPage(){
//        Given
        Pageable pageable = Pageable.ofSize(20);
//        When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null,pageable);
//        Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        BDDMockito.then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName(" 게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenHashtag_whenSearchingArticlesViaHashtag_thenReturnsArticlesPage(){
//        Given
        String hashtag= "#java";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));
//        When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtag,pageable);
//        Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        BDDMockito.then(articleRepository).should().findByHashtag(hashtag,pageable);
    }




    @DisplayName("게시글을 수를 조회하면, 게시글 수를 반환한다.")
    @Test
    void givenNothing_whenCountingArticles_thenReturnsArticlesCount(){
//        Given
        Long expected = 0L;
        given(articleRepository.count()).willReturn(expected)
//        When
        long actual = sut.getArticleCount();
//        Then
        assertThat(actual).isEqualTo(expected);
        BDDMockito.then(articleRepository).should().count();
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다.")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags(){
//        Given
        List<String> expectedHashtags = List.of("#java","#spring","#boot");
        given(articleRepository.findAllDistinctHashtags()).willReturn(expectedHashtags);

//        When
        List<String> actualHashtags = sut.getHashtags();

//        Then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        BDDMockito.then(articleRepository).should().findAllDistinctHashtags();
    }


    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNoneExistentArticleId_whenSearchingArticle_thenThrowsException(){
//        Given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.of())
//        When
        Throwable t = catchThrowable(() -> sut.getArticle(articleId));
//        Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId : " + articleId);
        BDDMockito.then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){

//        Given
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(createArticle);
//        When
        sut.saveArticle(dto);

//        Then
        BDDMockito.then(articleRepository).should().save(any(Article.class));

    }

    @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle(){

//        Given
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용 #springboot");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);

//        When
        sut.updateArticle(dto);
//        Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title",dto.title())
                .hasFieldOrPropertyWithValue("content",dto.content())
                .hasFieldOrPropertyWithValue("hashtag",dto.hashtag());
        BDDMockito.then(articleRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long articleId = 1L;
        String userId = "uno";
        given(articleRepository.getReferenceById(articleId)).willReturn(createArticle());
        willDoNothing().given(articleRepository).deleteByIdAndUserAccount_UserId(articleId, userId);
        willDoNothing().given(articleRepository).flush();
        willDoNothing().given(hashtagService).deleteHashtagWithoutArticles(any());

        // When
        sut.deleteArticle(1L, userId);

        // Then
        then(articleRepository).should().getReferenceById(articleId);
        then(articleRepository).should().deleteByIdAndUserAccount_UserId(articleId, userId);
        then(articleRepository).should().flush();
        then(hashtagService).should(times(2)).deleteHashtagWithoutArticles(any());
    }

}
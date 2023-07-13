package com.example.noticeboardproject.controller;

import com.example.noticeboardproject.config.SecurityConfig;
import com.example.noticeboardproject.domain.type.SearchType;
import com.example.noticeboardproject.dto.ArticleWithCommentsDto;
import com.example.noticeboardproject.dto.UserAccountDto;
import com.example.noticeboardproject.service.ArticleService;
import com.example.noticeboardproject.service.PaginationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View Controller")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;


    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnArticlesView() throws Exception {
//        Given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4));

//        When&Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearching_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
//        Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4));

//        When&Then
        mvc.perform(get("/articles")
                        .queryParam("searchType", searchValue.toString())
                        .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정렬 기능")
    @Test
    public void givenPagingAndSortingParams_whenSearchingArticlesPages_thenReturnArticlesView() throws Exception {
//        Given

        String sortName = "title";
        String direction ="desc";
        int pageNumber = 0;
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1,2,3,4,5);


        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4));

//        When&Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

    }
    @DisplayName("[view][GET] 게시글 상세페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticleView() throws Exception {
//        Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
//        When&Then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
        then(articleService).should().getArticle(articleId);

    }
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnArticleSearchView() throws Exception {
//        Given
//        When&Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));


    }

    @DisplayName("[view][GET] 게시글 해시테그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashTagSearchView_thenReturnArticleHashTagSearchView() throws Exception {
//        Given
        given(articleService.searchArticlesViaHashtag(eq(null),any(Pageable.class))).willReturn(Page.empty());
//        When&Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attributeExists("hashtag"))
                .andExpect(model().attributeExists("paginationBarNumber"));
        then(articleService).should().searchArticlesViaHashtag(eq(null),any(Pageable.class));

    }

    @DisplayName("[view][GET] 게시글 해시테그 검색 페이지 - 정상 호출, 해시태그 입력")
    @Test
    public void givenHashtag_whenRequestingArticleHashTagSearchView_thenReturnArticleHashTagSearchView() throws Exception {
//        Given
        String hashtag = "#java";
        given(articleService.searchArticlesViaHashtag(eq(hashtag),any(Pageable.class))).willReturn(Page.empty());
//        When&Then
        mvc.perform(get("/articles/search-hashtag")
                        .queryParam("searchValue",hashtag))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attributeExists("hashtag"))
                .andExpect(model().attributeExists("paginationBarNumber"));
        then(articleService).should().searchArticlesViaHashtag(eq(hashtag),any(Pageable.class));

    }

    private ArticleWithCommentsDto createArticleWithCommentsDto(){
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "kyuhyun",
                LocalDateTime.now(),
                "kyuhyun"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "kyuhyun",
                "pw",
                "kyuhyun@mail.com",
                "kyuhyun",
                "memo",
                LocalDateTime.now(),
                "kyuhyun",
                LocalDateTime.now(),
                "kyuhyun"
        );
    }
    }

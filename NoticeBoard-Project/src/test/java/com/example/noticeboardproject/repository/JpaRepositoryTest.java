package com.example.noticeboardproject.repository;

import com.example.noticeboardproject.config.JpaConfig;
import com.example.noticeboardproject.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//Slice Test 를 할꺼임
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) //-> jpaConfig 의 존재를 모르기때문에(내가 만든거기떄문) 저걸 입력해야함
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    //밑에는 생성자 주입 패턴입
    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository)
    {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select Test")
    @Test
    void givenTestData_whenSelecting_thenWorkFine(){
//        Given

//        When
        List<Article> articles = articleRepository.findAll();
//        Then
        assertThat(articles)
                .isNotNull()
                .hasSize(1000);

    }

    @DisplayName("insert Test")
    @Test
    void givenTestData_whenInserting_theWorkFine(){
//        Given
        long previousCount = articleRepository.count();

//        when
        Article savedArticle = articleRepository.save(Article.of("new article", "new content","#spring"));

//        Then
        assertThat(articleRepository.count()).isEqualTo(previousCount+1);

    }

    @DisplayName("update Test")
    @Test
    void givenTestData_whenUpdating_theWorkFine(){
//        Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashTag(updatedHashtag);

//        when
        Article savedArticle = articleRepository.saveAndFlush(article);


//        Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);

    }

    @DisplayName("delete Test")
    @Test
    void givenTestData_whenDeleting_theWorkFine(){
//        Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();

//        when
        articleRepository.delete(article);

//        Then
        assertThat(articleRepository.count()).isEqualTo((previousArticleCount - 1));
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentSize);
    }
}
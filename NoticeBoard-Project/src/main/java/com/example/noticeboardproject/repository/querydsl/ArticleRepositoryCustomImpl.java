package com.example.noticeboardproject.repository.querydsl;

import com.example.noticeboardproject.domain.Article;
import com.example.noticeboardproject.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {
    public ArticleRepositoryCustomImpl(Class<?> domainClass) {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

        JPQLQuery<String> query = from(article)
                .distinct()
                .select(article.hashTag)
                .where(article.hashTag.isNotNull());

        return query.fetch();
    }
}

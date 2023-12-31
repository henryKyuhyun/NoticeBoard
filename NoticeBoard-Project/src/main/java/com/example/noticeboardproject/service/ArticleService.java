package com.example.noticeboardproject.service;

import com.example.noticeboardproject.domain.Article;
import com.example.noticeboardproject.domain.UserAccount;
import com.example.noticeboardproject.domain.type.SearchType;
import com.example.noticeboardproject.dto.ArticleDto;
import com.example.noticeboardproject.dto.ArticleWithCommentsDto;
import com.example.noticeboardproject.repository.ArticleRepository;
import com.example.noticeboardproject.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;


    @Transactional(readOnly = true) // Searching 은 Reading 하는 거고 변경하는게 아니기떄문에 readOnly 값을 준다.
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }



        return switch(searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#"+searchKeyword, pageable).map(ArticleDto::from);
        };
    }
    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId){
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(()-> new EntityNotFoundException("게시글이 없습니다 - articleId : "+ articleId));
    }

    public void saveArticle(ArticleDto dto){
        articleRepository.save(dto.toEntity());

    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try{
            Article article = articleRepository.getReferenceById(articleId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            if(article.getUserAccount().equals(userAccount))
            if (dto.title() != null){article.setTitle(dto.title());}
            if (dto.content() != null){article.setContent(dto.content());}
            article.setHashTag(dto.hashtag());
        } catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - dto : {} " , e.getLocalizedMessage());
        }

    }

    public void deleteArticle(long articleId, String userId)
    {
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
        if (hashtag ==null || hashtag.isBlank()){
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag,pageable).map(ArticleDto::from);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}

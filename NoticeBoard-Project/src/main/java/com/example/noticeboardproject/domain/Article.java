package com.example.noticeboardproject.domain;

import com.example.noticeboardproject.config.JpaConfig;
import com.example.noticeboardproject.dto.ArticleDto;
import com.example.noticeboardproject.dto.UserAccountDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

//Setter 은 사용하지 않는게 좋다 그래서 필요한것들만 각각에 건다
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdBy"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private UserAccount userAccount;   // User정보(ID)

    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 10000)private String content;
    @Setter private String hashTag;

    //밑의 용도 -> list, set, map 으로 해도되고 용도에따라다름, 이 아티클에 연동되어있는 커멘트는 중복을 허용하지않고 모아서 컬렉션으로 보겠다는 의미

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;
    @CreatedBy @Column(nullable = false,length = 100) private String createdBy;
    @LastModifiedDate @Column(nullable = false)private LocalDateTime modifiedAt;
    @LastModifiedBy @Column(nullable = false,length = 100) private String modifiedBy;


//    jpa entity 는 기본생성자를 가지고 있어야한다.
    protected Article(){};

    private Article(UserAccount userAccount, String title, String content, String hashTag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashTag = hashTag;
    }
//밑의 팩토링메소드를 통해서 new키워드를 사용안해도 사용할 수 있게 한거다.
public static ArticleDto of(UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos) {
    return new ArticleDto(null, userAccountDto, title, content, hashtagDtos, null, null, null, null);
}

    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleDto(id, userAccountDto, title, content, hashtagDtos, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

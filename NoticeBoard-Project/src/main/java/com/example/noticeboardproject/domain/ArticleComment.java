package com.example.noticeboardproject.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),

})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter @ManyToOne(optional = false) private Article article; // 게시글(ID)
//    @Setter private Long article; 이렇게해도 연관관계 조인 할 수 있음
    @Setter @Column(nullable = false, length = 500) private String content;
    @CreatedDate
    @Column(nullable = false) private LocalDateTime createdAt;
    @CreatedBy
    @Column(nullable = false,length = 100) private String createdBy;
    @LastModifiedDate
    @Column(nullable = false)private LocalDateTime modifiedAt;
    @LastModifiedBy
    @Column(nullable = false,length = 100) private String modifiedBy;

    //밑에 생성자는 NoArgsConstructor 로 만들 수있음 . 상관없음
    protected ArticleComment(){};

    private ArticleComment(Article article,UserAccount userAccount, String content){
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static ArticleComment of(Article article,UserAccount userAccount ,String content){
        return new ArticleComment(article,userAccount,content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package com.example.noticeboardproject.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Article {

    private Long id;
    private String title;
    private String content;
    private String hashTag;

    private LocalDateTime createdAt;
    private String createBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}

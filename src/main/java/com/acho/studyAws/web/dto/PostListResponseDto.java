package com.acho.studyAws.web.dto;

import com.acho.studyAws.domain.BaseTimeEntity;
import com.acho.studyAws.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto{

    private Long id;
    private String title;
    private String author;
    private LocalDateTime updatedDate;


    public PostListResponseDto(Posts post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.updatedDate = post.getUpdatedDate();
    }
}

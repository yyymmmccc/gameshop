package com.project.game.entity;

import com.project.game.dto.request.comment.CommentRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comment")
@Table(name = "comment")
@Builder
public class CommentEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    private String content;

    @CreationTimestamp
    @Column(name = "created_date")
    private String createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private String updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private UserEntity userEntity;

    public void update(CommentRequestDto dto){
        this.content = dto.getContent();
    }


}

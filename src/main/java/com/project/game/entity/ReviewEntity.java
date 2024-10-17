package com.project.game.entity;

import com.project.game.dto.request.review.ReviewPatchRequestDto;
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
@Entity(name = "review")
@Table(name = "review")
@Builder
public class ReviewEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;

    private int rating;
    private String content;

    @CreationTimestamp
    @Column(name = "created_date")
    private String createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private String updatedDate;

    public void update(ReviewPatchRequestDto dto){
        this.rating = dto.getRating();
        this.content = dto.getContent();
    }
}

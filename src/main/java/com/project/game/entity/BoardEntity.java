package com.project.game.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "board")
@Table(name = "board")
@Builder
public class BoardEntity {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardId;

    private String title;
    private String content;
    private int hit;

    @CreationTimestamp
    @Column(name = "created_date")
    private String createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private String updatedDate;

    @Column(name = "comment_count")
    private int commentCount;

    @Column(name = "favorite_count")
    private int favoriteCount;

    @Column(name = "category_id")
    private int categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")  // board Table 에 있는 외래키 user_email
    private UserEntity userEntity;

    // cascade
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImageEntity> boardImageEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteEntity> favoriteEntitieList = new ArrayList<>();

    public void update(String title, String content, int categoryId){
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;

    }

    public void incViewCount() {
        hit++;
    }

    public void incCommentCount(){
        commentCount++;
    }

    public void decCommentCount(){
        commentCount--;
    }

    public void incFavoriteCount() {
        favoriteCount++;
    }

    public void decFavoriteCount(){
        favoriteCount--;
    }
}

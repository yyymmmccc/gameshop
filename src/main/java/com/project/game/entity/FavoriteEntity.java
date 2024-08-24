package com.project.game.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "favorite")
@Table(name = "favorite")
@Builder
public class FavoriteEntity {

    @Id
    @Column(name = "favorite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int favoriteId;

    @Column(name = "reaction")
    private int reaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    public FavoriteEntity(BoardEntity boardEntity, UserEntity userEntity) {
        this.boardEntity = boardEntity;
        this.userEntity = userEntity;
        this.reaction = 1;
    }
}

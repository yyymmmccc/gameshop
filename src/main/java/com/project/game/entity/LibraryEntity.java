package com.project.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity(name = "library")
@Table(name = "library")
@NoArgsConstructor
@AllArgsConstructor
public class LibraryEntity {

    @Id
    @Column(name = "library_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int libraryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;

    @Column(name = "add_date")
    @CreationTimestamp
    private String addDate;

    public LibraryEntity(UserEntity userEntity, GameEntity gameEntity) {
        this.userEntity = userEntity;
        this.gameEntity = gameEntity;
    }
}

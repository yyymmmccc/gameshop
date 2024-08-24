package com.project.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "game_image")
@Table(name = "game_image")
public class GameImageEntity {

    @Id
    @Column(name = "game_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;

    @Column(name = "game_image_url")
    private String gameImageUrl;

    private String thumbnail;
}

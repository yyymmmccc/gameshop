package com.project.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "game_category_mapping")
@Table(name = "game_category_mapping")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameCategoryMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_category_mapping_id")
    private int gameCategoryMappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private GameCategoryEntity gameCategoryEntity;

}

package com.project.game.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "board_image")
@Table(name = "board_image")
public class BoardImageEntity {

    @Id
    @Column(name = "board_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @Column(name = "board_image_url")
    private String imageUrl;

}

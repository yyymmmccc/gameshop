package com.project.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Table(name = "cart")
@Entity(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;

    @CreationTimestamp
    @Column(name = "add_date")
    private String addDate;

    public CartEntity(UserEntity userEntity, GameEntity gameEntity) {
        this.userEntity = userEntity;
        this.gameEntity = gameEntity;
    }
}

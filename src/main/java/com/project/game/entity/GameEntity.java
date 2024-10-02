package com.project.game.entity;

import com.project.game.dto.request.game.AdminPostGameRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "game")
@Table(name = "game")
public class GameEntity {

    @Id
    @Column(name = "game_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private GameCategoryEntity gameCategoryEntity;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "game_dc")
    private String gameDc;

    private String publisher;

    @Column(name = "original_price")
    private int originalPrice;

    @Column(name = "discount_price")
    private int discountPrice;

    @Column(name = "discount_percentage")
    private int discountPercentage;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "release_date")
    private String releaseDate;

    @CurrentTimestamp
    @Column(name = "reg_date")
    private String regDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private String updatedDate;

    @Column(name = "purchase_count")
    private int purchaseCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "gameEntity", fetch = FetchType.LAZY , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameImageEntity> gameImageEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "gameEntity", fetch = FetchType.LAZY , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> ReviewEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "gameEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartEntity> cartEntityList = new ArrayList<>();

    public void update(AdminPostGameRequestDto dto, GameCategoryEntity gameCategoryEntity) {
        this.gameCategoryEntity = gameCategoryEntity;
        this.gameName = dto.getGameName();
        this.gameDc = dto.getGameDc();
        this.publisher = dto.getPublisher();
        this.originalPrice = dto.getOriginalPrice();
        this.discountPrice = dto.getOriginalPrice() * (1 - dto.getDiscountPercentage() / 100);
        this.discountPercentage = dto.getDiscountPercentage();
        this.releaseDate = dto.getReleaseDate();
    }

    public void incReviewCount() {
        reviewCount++;
    }

    public void decReviewCount() {
        reviewCount--;
    }

    public void incPurchaseCount(){
        purchaseCount++;
    }
}

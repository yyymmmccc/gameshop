package com.project.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "game_specifications")
@Entity(name = "game_specifications")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSpecificationsEntity {

    @Id
    @Column(name = "game_specifications_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameSpecificationsId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private GameEntity gameEntity;

    @Column(name = "windows_min_specifications")
    private String windowsMinSpecifications;

    @Column(name = "windows_max_specifications")
    private String windowsMaxSpecifications;

    @Column(name = "mac_min_specifications")
    private String macMinSpecifications;

    @Column(name = "mac_max_specifications")
    private String macMaxSpecifications;

    @Column(name = "linux_min_specifications")
    private String linuxMinSpecifications;

    @Column(name = "linux_max_specifications")
    private String linuxMaxSpecifications;


}

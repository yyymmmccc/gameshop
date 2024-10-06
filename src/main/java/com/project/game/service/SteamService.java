package com.project.game.service;

import org.springframework.http.ResponseEntity;

public interface SteamService {


    ResponseEntity<?> regSteamGame(long gameId);
}

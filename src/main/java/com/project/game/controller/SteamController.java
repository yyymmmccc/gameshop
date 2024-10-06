package com.project.game.controller;

import com.project.game.service.SteamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SteamController {

    private final SteamService steamService;

    @GetMapping("/game")
    public ResponseEntity<?> regSteamGame(@RequestParam("gameId") long gameId){

        return steamService.regSteamGame(gameId);
    }
}
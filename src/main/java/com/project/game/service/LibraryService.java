package com.project.game.service;

import org.springframework.http.ResponseEntity;

public interface LibraryService {

    ResponseEntity getLibraryList(String email);
}

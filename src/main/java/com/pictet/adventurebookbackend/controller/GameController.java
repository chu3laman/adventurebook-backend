package com.pictet.adventurebookbackend.controller;

import com.pictet.adventurebookbackend.dto.ChoiceRequest;
import com.pictet.adventurebookbackend.dto.GameState;
import com.pictet.adventurebookbackend.dto.StartRequest;
import com.pictet.adventurebookbackend.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/api/game")
public class GameController {

    private GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<GameState> startGame(@RequestBody StartRequest request) {
        GameState state = gameService.startGame(request.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(state);
    }

    @PostMapping("/{playerId}/choose")
    public ResponseEntity<GameState> chooseOption(
            @PathVariable Long playerId,
            @RequestBody ChoiceRequest request) {
        GameState state = gameService.chooseOption(playerId, request.getGotoId());
        return ResponseEntity.ok(state);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<GameState> getGameState(@PathVariable Long playerId) {
        GameState state = gameService.getGameState(playerId);
        return ResponseEntity.ok(state);
    }
}

package com.devcom.puzzles.controller;

import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.request.PuzzleDataRequest;
import com.devcom.puzzles.exception.CannotAssemblePuzzleException;
import com.devcom.puzzles.model.GameSession;
import com.devcom.puzzles.service.AssemblerService;
import com.devcom.puzzles.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/assembler")
@RequiredArgsConstructor
public class AssemblerController {

    private final AssemblerService assemblerService;
    private final GameSessionService gameSessionService;

    @PostMapping
    public ResponseEntity<?> assemblePuzzle(@RequestBody PuzzleDataRequest puzzleDataRequest) {
        String sessionId = puzzleDataRequest.sessionId();
        log.info("{}", sessionId);
        try {
            List<PuzzleEntry> assembled = assemblerService.assemblePuzzle(puzzleDataRequest);

            return ResponseEntity.ok(assembled);
        } catch (CannotAssemblePuzzleException e) {
            GameSession session = gameSessionService.getSession(sessionId);
            String errorMessage = "Cannot assemble puzzle for image id " + session.getImageId();
            log.error("{}", errorMessage);

            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}

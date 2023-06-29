package com.devcom.puzzles.controller;

import com.devcom.puzzles.dto.request.PuzzleDataRequest;
import com.devcom.puzzles.dto.response.PuzzlesDataResponse;
import com.devcom.puzzles.service.PuzzleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/puzzles")
@RequiredArgsConstructor
public class PuzzleController {

    private final PuzzleService puzzleService;

    @GetMapping
    public ResponseEntity<PuzzlesDataResponse> getPuzzles(@RequestParam String imageId) {
        log.info("got request image sessionId ={}", imageId);
        PuzzlesDataResponse puzzlesDataResponse = puzzleService.processImageAndGetPuzzles(imageId);


        return ResponseEntity.ok(puzzlesDataResponse);
    }

    @PostMapping("/check-complete")
    public ResponseEntity<Boolean> isCompleted(@RequestBody PuzzleDataRequest puzzleDataRequest) {
        boolean completed = puzzleService.isCompleted(puzzleDataRequest);


        return ResponseEntity.ok(completed);
    }

}
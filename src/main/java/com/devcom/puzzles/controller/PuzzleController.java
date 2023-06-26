package com.devcom.puzzles.controller;

import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.PuzzlesData;
import com.devcom.puzzles.service.AssemblerService;
import com.devcom.puzzles.service.PuzzleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/puzzles")
@RequiredArgsConstructor
public class PuzzleController {

    private final PuzzleService puzzleService;

    @GetMapping
    public ResponseEntity<PuzzlesData> getPuzzles(@RequestParam String imageId) {
        log.info("got request image id ={}", imageId);
        PuzzlesData puzzlesData = puzzleService.processImageAndGetPuzzles(imageId);


        return ResponseEntity.ok(puzzlesData);
    }

    @PostMapping("/check-complete")
    public ResponseEntity<Boolean> isCompleted(@RequestBody List<PuzzleEntry> puzzles) {
        boolean completed = puzzleService.isCompleted(puzzles);


        return ResponseEntity.ok(completed);
    }

}
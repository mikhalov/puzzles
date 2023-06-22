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
    private final AssemblerService assemblerService;

    @GetMapping
    public ResponseEntity<PuzzlesData> getPuzzles(@RequestParam int rows, @RequestParam int cols) {
        PuzzlesData puzzlesData = puzzleService.processImageAndGetPuzzles(rows, cols);


        return ResponseEntity.ok(puzzlesData);
    }

    @PostMapping("/check-complete")
    public ResponseEntity<Boolean> isCompleted(@RequestBody List<PuzzleEntry> puzzles) {
        boolean completed = puzzleService.isCompleted(puzzles);


        return ResponseEntity.ok(completed);
    }

}
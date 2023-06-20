package com.devcom.puzzles.controller;

import com.devcom.puzzles.service.PuzzleService;
import com.devcom.puzzles.dto.ImageSize;
import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.PuzzleData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.devcom.puzzles.dto.Location;
import org.opencv.core.Mat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.devcom.puzzles.util.MapUtil;

import java.util.*;


@Slf4j
@RestController
@RequestMapping("/api/puzzles")
@RequiredArgsConstructor
public class PuzzleController {

    private final PuzzleService puzzleService;

    @GetMapping
    public ResponseEntity<PuzzleData> getPuzzles(@RequestParam int rows, @RequestParam int cols) {
        log.info("Front call");
        Map<Location, Mat> puzzles = LinkedHashMap.newLinkedHashMap(rows * cols);
        ImageSize size = puzzleService.splitImageAndGetSize(rows, cols, puzzles);
        puzzleService.setSnapshot(puzzleService.convertToBase64(puzzles));

        puzzles = MapUtil.shuffleMapValues(puzzles, new LinkedHashMap<>());

        List<PuzzleEntry> entries = puzzleService.convertToBase64(puzzles);
        PuzzleData puzzleData = new PuzzleData(size, entries);

        return ResponseEntity.ok(puzzleData);
    }

    @PostMapping("/check-complete")
    public ResponseEntity<Boolean> isCompleted(@RequestBody List<PuzzleEntry> puzzles) {
        boolean completed = puzzleService.isCompleted(puzzles);

        return ResponseEntity.ok(completed);
    }

}
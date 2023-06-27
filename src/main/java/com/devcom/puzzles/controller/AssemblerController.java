package com.devcom.puzzles.controller;

import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.request.PuzzleDataRequest;
import com.devcom.puzzles.service.AssemblerService;
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

    @PostMapping
    public ResponseEntity<List<PuzzleEntry>> assemblePuzzle(@RequestBody PuzzleDataRequest puzzleDataRequest) {
        log.info("{}", puzzleDataRequest.sessionId());
        List<PuzzleEntry> assembled = assemblerService.assemblePuzzle(puzzleDataRequest);

        return ResponseEntity.ok(assembled);
    }
}

package com.devcom.puzzles.service;

import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.PuzzlesData;

import java.util.List;

public interface PuzzleService {

    boolean isCompleted(List<PuzzleEntry> puzzleEntries);

    PuzzlesData processImageAndGetPuzzles(String imageId);
}

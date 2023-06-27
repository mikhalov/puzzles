package com.devcom.puzzles.service;

import com.devcom.puzzles.dto.request.PuzzleDataRequest;
import com.devcom.puzzles.dto.response.PuzzlesDataResponse;

public interface PuzzleSessionService {

    boolean isCompleted(PuzzleDataRequest puzzleDataRequest);

    PuzzlesDataResponse processImageAndGetPuzzles(String imageId);
}

package com.devcom.puzzles.dto.response;

import com.devcom.puzzles.dto.ImageSize;
import com.devcom.puzzles.dto.PuzzleEntry;

import java.util.List;

public record PuzzlesDataResponse(String sessionId, ImageSize size, List<PuzzleEntry> entries) {
}

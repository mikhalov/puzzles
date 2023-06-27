package com.devcom.puzzles.dto.request;

import com.devcom.puzzles.dto.PuzzleEntry;

import java.util.List;

public record PuzzleDataRequest(String sessionId, List<PuzzleEntry> entries) {
}

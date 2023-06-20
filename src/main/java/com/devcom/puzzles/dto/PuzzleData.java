package com.devcom.puzzles.dto;

import java.util.List;

public record PuzzleData(ImageSize size, List<PuzzleEntry> entries) {
}

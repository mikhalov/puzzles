package com.devcom.puzzles.service;

import com.devcom.puzzles.dto.ImageSize;
import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
import org.opencv.core.Mat;

import java.util.List;
import java.util.Map;

public interface PuzzleService {

    ImageSize splitImageAndGetSize(int rows, int cols, Map<Location, Mat> puzzles);

    List<PuzzleEntry> convertToBase64(Map<Location, Mat> puzzles);

    boolean isCompleted(List<PuzzleEntry> puzzleEntries);

    void setSnapshot(List<PuzzleEntry> entries);

    List<PuzzleEntry> getSnapshot();

}

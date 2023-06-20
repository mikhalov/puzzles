package com.devcom.puzzles.util;

import com.devcom.puzzles.constant.Edge;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.devcom.puzzles.dto.Location;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.devcom.puzzles.constant.Edge.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PuzzleEdgeMatcher {
    public static Map<Location, Edge> getAdjacentPuzzlesLocations(Location toCompare, Map<Location, Mat> puzzles) {
        Mat puzzleToCompare = puzzles.get(toCompare);
        HashMap<Location, Edge> result = new HashMap<>();

        putAdjacent(puzzles, puzzleToCompare, RIGHT, LEFT, result);
        putAdjacent(puzzles, puzzleToCompare, LEFT, RIGHT, result);
        putAdjacent(puzzles, puzzleToCompare, TOP, BOTTOM, result);
        putAdjacent(puzzles, puzzleToCompare, BOTTOM, TOP, result);

        return result;
    }

    private static void putAdjacent(Map<Location, Mat> puzzles, Mat puzzleToCompare, Edge edgeToCompare,
                             Edge comparableEdge, HashMap<Location, Edge> result) {
        puzzles.entrySet()
                .stream()
                .max(Comparator.comparingDouble(e -> arePuzzleEdgesAdjacent(
                        puzzleToCompare,
                        edgeToCompare,
                        e.getValue(),
                        comparableEdge
                )))
                .ifPresent(e -> result.put(e.getKey(), edgeToCompare));
    }

    private static double arePuzzleEdgesAdjacent(Mat firstPuzzle, Edge firstEdge, Mat secondPuzzle, Edge secondEdge) {
        Mat edge1 = extractEdge(firstPuzzle, firstEdge);
        Mat edge2 = extractEdge(secondPuzzle, secondEdge);

        return compareEdges(edge1, edge2);
    }

    private static Mat extractEdge(Mat puzzle, Edge edge) {
        return switch (edge) {
            case TOP -> puzzle.row(0);
            case BOTTOM -> puzzle.row(puzzle.rows() - 1);
            case LEFT -> puzzle.col(0);
            case RIGHT -> puzzle.col(puzzle.cols() - 1);
        };
    }

    private static double compareEdges(Mat firstEdge, Mat secondEdge) {
        Mat result = new Mat();
        int method = Imgproc.TM_CCOEFF_NORMED;
        Imgproc.matchTemplate(firstEdge, secondEdge, result, method);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        return mmr.minVal;
    }
}

package com.devcom.puzzles.util;

import com.devcom.puzzles.constant.Edge;
import com.devcom.puzzles.dto.Location;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

import static com.devcom.puzzles.constant.Edge.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PuzzleEdgeMatcher {
    public static Optional<Location> getAdjacentPuzzlesLocation(
            Location toCompare, Map<Location, Mat> puzzles, Edge edge) {
        Mat puzzleToCompare = puzzles.get(toCompare);

        return getAdjacent(puzzles, puzzleToCompare, edge);
    }

    private static Optional<Location> getAdjacent(
            Map<Location, Mat> puzzles, Mat puzzleToCompare, Edge edgeToCompare) {
        Edge comparableEdge = getOppositEdge(edgeToCompare);
        var list = puzzles.entrySet()
                .stream()
                .filter(p -> arePuzzleEdgesAdjacent(
                        puzzleToCompare,
                        edgeToCompare,
                        p.getValue(),
                        comparableEdge)
                )
                .toList();

        if (list.size() > 1) {
            log.warn("more than 1 match {}", list);

            return list.stream()
                    .max(Comparator.comparingDouble(e -> getProbabilityPuzzleEdgesAdjacent(
                            puzzleToCompare,
                            edgeToCompare,
                            e.getValue(),
                            comparableEdge
                    )))
                    .map(Map.Entry::getKey);
        }

        return list.stream()
                .findFirst()
                .map(Map.Entry::getKey);
    }

    public static Edge getOppositEdge(Edge edge) {
        return switch (edge) {
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case TOP -> BOTTOM;
            case BOTTOM -> TOP;
        };
    }

    private static double getProbabilityPuzzleEdgesAdjacent(
            Mat firstPuzzle, Edge firstEdge, Mat secondPuzzle, Edge secondEdge) {
        Mat edge1 = extractEdge(firstPuzzle, firstEdge);
        Mat edge2 = extractEdge(secondPuzzle, secondEdge);
        return compareEdges(edge1, edge2);
    }

    private static boolean arePuzzleEdgesAdjacent(
            Mat firstPuzzle, Edge firstEdge, Mat secondPuzzle, Edge secondEdge) {
        Mat edge1 = extractEdge(firstPuzzle, firstEdge);
        Mat edge2 = extractEdge(secondPuzzle, secondEdge);
        double result = compareEdges(edge1, edge2);
        return result > 0.9;
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

        return mmr.maxVal;
    }
}
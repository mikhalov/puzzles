package com.devcom.puzzles.util;

import com.devcom.puzzles.constant.Edge;
import com.devcom.puzzles.dto.Location;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.devcom.puzzles.constant.Edge.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PuzzleEdgeMatcher {

    private static final Map<List<Object>, Double> similarityCache = new ConcurrentHashMap<>();

    public static List<Edge> getMatchedEdges(Location toCompare, Map<Location, Mat> puzzles, double threshold) {
        Mat puzzleToCompare = puzzles.get(toCompare);

        return Arrays.stream(values())
                .parallel()
                .filter(e -> checkEdge(puzzles, puzzleToCompare, e, threshold))
                .toList();
    }

    private static boolean checkEdge(Map<Location, Mat> puzzles, Mat puzzleToCompare, Edge edgeToCompare, double threshold) {
        Edge comparableEdge = getOppositEdge(edgeToCompare);

        return puzzles.entrySet()
                .stream()
                .anyMatch(p -> arePuzzleEdgesAdjacent(
                        puzzleToCompare,
                        edgeToCompare,
                        p.getValue(),
                        comparableEdge,
                        threshold)
                );
    }

    public static Optional<Location> getAdjacentPuzzlesLocation(
            Location toCompare, Map<Location, Mat> puzzles, Edge edge) {
        Mat puzzleToCompare = puzzles.get(toCompare);

        return getAdjacent(puzzles, puzzleToCompare, edge);
    }

    public static Optional<Location> getAdjacentPuzzlesLocation(Location toCompare, Map<Location, Mat> puzzles,
                                                                Edge edge, double threshold) {
        Mat puzzleToCompare = puzzles.get(toCompare);

        return getAdjacent(puzzles, puzzleToCompare, edge, threshold);
    }

    private static Optional<Location> getAdjacent(
            Map<Location, Mat> puzzles, Mat puzzleToCompare, Edge edgeToCompare, double threshold) {
        Edge comparableEdge = getOppositEdge(edgeToCompare);
        var list = puzzles.entrySet()
                .stream()
                .filter(p -> arePuzzleEdgesAdjacent(
                        puzzleToCompare,
                        edgeToCompare,
                        p.getValue(),
                        comparableEdge,
                        threshold)
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


    private static Optional<Location> getAdjacent(
            Map<Location, Mat> puzzles, Mat puzzleToCompare, Edge edgeToCompare) {
        Edge comparableEdge = getOppositEdge(edgeToCompare);
        return puzzles.entrySet()
                .stream()
                .max(Comparator.comparingDouble(
                        e -> getProbabilityPuzzleEdgesAdjacent(
                                puzzleToCompare,
                                edgeToCompare,
                                e.getValue(),
                                comparableEdge
                        )))
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

    @Cacheable(
            value = "edgeSimilarity",
            key = "#firstPuzzle.hashCode() + #firstEdge.hashCode() + #secondPuzzle.hashCode() + #secondEdge.hashCode()"
    )
    private static double getProbabilityPuzzleEdgesAdjacent(Mat firstPuzzle, Edge firstEdge,
                                                            Mat secondPuzzle, Edge secondEdge) {
        Mat edge1 = extractEdge(firstPuzzle, firstEdge);
        Mat edge2 = extractEdge(secondPuzzle, secondEdge);
        return compareEdges(edge1, edge2);
    }

    private static double getProbabilityPuzzleEdgesAdjacent1(Mat firstPuzzle, Edge firstEdge, Mat secondPuzzle, Edge secondEdge) {
        List<Object> key = List.of(firstPuzzle, firstEdge, secondPuzzle, secondEdge);

        // Return the cached value if it exists
        if (similarityCache.containsKey(key)) {
            return similarityCache.get(key);
        }

        // Otherwise, calculate the value
        Mat edge1 = extractEdge(firstPuzzle, firstEdge);
        Mat edge2 = extractEdge(secondPuzzle, secondEdge);
        double similarity = compareEdges(edge1, edge2);

        // Cache the calculated value
        similarityCache.put(key, similarity);

        return similarity;
    }

    private static boolean arePuzzleEdgesAdjacent(Mat firstPuzzle, Edge firstEdge,
                                                  Mat secondPuzzle, Edge secondEdge, double threshold) {
        double result = getProbabilityPuzzleEdgesAdjacent(firstPuzzle, firstEdge, secondPuzzle, secondEdge);
        return result > threshold;
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
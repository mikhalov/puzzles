package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.constant.Edge;
import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.exception.PuzzleNotFoundException;
import com.devcom.puzzles.service.AssemblerService;
import com.devcom.puzzles.util.ImageConvertor;
import com.devcom.puzzles.util.PuzzleEdgeMatcher;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AssemblerServiceImpl implements AssemblerService {
    private static final double THRESHOLD = 0.7;

    @Override
    public List<PuzzleEntry> assemblePuzzple(List<PuzzleEntry> entries) {
        Map<Location, Mat> puzzles = ImageConvertor.convertToMatMap(entries);

        Map<Location, Mat> map = assemblePuzzle(puzzles);

        return ImageConvertor.convertToPuzzleEntryList(map);
    }

    private Map<Location, Mat> assemblePuzzle(Map<Location, Mat> puzzles) {
        double threshold = THRESHOLD;
        LinkedHashMap<Location, Mat> assembledPuzzles = new LinkedHashMap<>();

        Optional<Location> topLeftCorner = Optional.empty();

        long s = System.currentTimeMillis();
        while (topLeftCorner.isEmpty() && (threshold >= 0 || threshold <= 1)) {
            System.out.println("top left corn iteration, threshold= " + threshold);
            topLeftCorner = findTopLeftCorner(puzzles, threshold);
            threshold = threshold + 0.01;
        }
        long f = System.currentTimeMillis();
        System.out.println((f-s) + "c");
        Location currentLocation = topLeftCorner.orElseThrow(PuzzleNotFoundException::new);

        Location startOfRow = currentLocation;
        Set<Location> visitedLocations = new HashSet<>();
        Iterator<Location> locationToSwap = puzzles.keySet().iterator();

        while (currentLocation != null) {
            assembledPuzzles.put(locationToSwap.next(), puzzles.get(currentLocation));
            visitedLocations.add(currentLocation);

            Optional<Location> rightNeighbor = PuzzleEdgeMatcher
                    .getAdjacentPuzzlesLocation(currentLocation, puzzles, Edge.RIGHT, threshold);

            if (rightNeighbor.isPresent() && !visitedLocations.contains(rightNeighbor.get())) {
                currentLocation = rightNeighbor.get();
            } else {
                Optional<Location> belowStartOfRow = PuzzleEdgeMatcher
                        .getAdjacentPuzzlesLocation(startOfRow, puzzles, Edge.BOTTOM, threshold);
                if (belowStartOfRow.isPresent() && !visitedLocations.contains(belowStartOfRow.get())) {
                    startOfRow = belowStartOfRow.get();
                    currentLocation = startOfRow;
                } else {
                    currentLocation = null;
                }
            }
        }

        return assembledPuzzles;
    }

    private static Optional<Location> findTopLeftCorner(Map<Location, Mat> puzzles, double threshold) {
        return puzzles.keySet().parallelStream()
                .filter(l -> {
                    List<Edge> matchedEdges = PuzzleEdgeMatcher.getMatchedEdges(l, puzzles, threshold);
                    return matchedEdges.size() == 2 && matchedEdges.containsAll(List.of(Edge.RIGHT, Edge.BOTTOM));
                })
                .findFirst();
    }

}

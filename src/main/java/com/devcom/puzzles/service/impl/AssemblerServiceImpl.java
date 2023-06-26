package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.constant.Edge;
import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.exception.CannotAssemblePuzzleException;
import com.devcom.puzzles.exception.PuzzleNotFoundException;
import com.devcom.puzzles.service.AssemblerService;
import com.devcom.puzzles.service.PuzzleService;
import com.devcom.puzzles.util.ImageConvertor;
import com.devcom.puzzles.util.PuzzleEdgeMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssemblerServiceImpl implements AssemblerService {
    private static final double THRESHOLD = 0.75;
    private final PuzzleService puzzleService;

    @Override
    public List<PuzzleEntry> assemblePuzzle(List<PuzzleEntry> entries) {
        double threshold = THRESHOLD;
        Map<Location, Mat> puzzles = ImageConvertor.convertToMatMap(entries);

        long s = System.currentTimeMillis();
        while (threshold <= 1) {
            try {
                log.info("threshold= {}", threshold);
                Map<Location, Mat> map = assemblePuzzle(puzzles, threshold);
                List<PuzzleEntry> puzzleEntries = ImageConvertor.convertToPuzzleEntryList(map);
                if (puzzleService.isCompleted(puzzleEntries)) {
                    long f = System.currentTimeMillis();
                    System.out.println("\n" + (f-s) + "c\n");
                    return puzzleEntries;
                }

            } catch (PuzzleNotFoundException e) {
                log.warn("left top corner not found");
            } finally {
                threshold += 0.01;
            }

        }
        throw new CannotAssemblePuzzleException();
    }

    private Map<Location, Mat> assemblePuzzle(Map<Location, Mat> puzzles, double threshold) throws PuzzleNotFoundException {
        LinkedHashMap<Location, Mat> assembledPuzzles = new LinkedHashMap<>();

        Location currentLocation = findTopLeftCorner(puzzles, threshold)
                .orElseThrow(PuzzleNotFoundException::new);

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

package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.constant.Edge;
import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
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

    @Override
    public List<PuzzleEntry> assemble(List<PuzzleEntry> entries) {
        Map<Location, Mat> puzzles = ImageConvertor.convertToMatMap(entries);
        Map<Location, Mat> map = assemblePuzzle(puzzles);
        return ImageConvertor.convertToPuzzleEntryList(map);
    }

    // Створюємо копію вхідних даних, щоб не змінювати оригінальну мапу
    public Map<Location, Mat> assemblePuzzle(Map<Location, Mat> puzzles) {
        LinkedHashMap<Location, Mat> assembledPuzzles = new LinkedHashMap<>();

        // Step 1: Find the top-left corner piece
        Location currentLocation = findTopLeftCorner(puzzles);

        Location startOfRow = currentLocation;
        Set<Location> visitedLocations = new HashSet<>();

        while (currentLocation != null) {
            assembledPuzzles.put(currentLocation, puzzles.get(currentLocation));
            visitedLocations.add(currentLocation);

            // Step 3: Try to find the piece to the right
            Optional<Location> rightNeighbor = PuzzleEdgeMatcher.getAdjacentPuzzlesLocation(currentLocation, puzzles, Edge.RIGHT);

            if (rightNeighbor.isPresent() && !visitedLocations.contains(rightNeighbor.get())) {
                currentLocation = rightNeighbor.get();
            } else {
                // Step 4: Go to the next row
                Optional<Location> belowStartOfRow = PuzzleEdgeMatcher.getAdjacentPuzzlesLocation(startOfRow, puzzles, Edge.BOTTOM);

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

    private static Location findTopLeftCorner(Map<Location, Mat> puzzles) {
        for (Location location : puzzles.keySet()) {
            boolean hasLeftNeighbor = PuzzleEdgeMatcher.getAdjacentPuzzlesLocation(location, puzzles, Edge.LEFT).isPresent();
            boolean hasTopNeighbor = PuzzleEdgeMatcher.getAdjacentPuzzlesLocation(location, puzzles, Edge.TOP).isPresent();

            if (!hasLeftNeighbor && !hasTopNeighbor) {
                return location;
            }
        }
        throw new RuntimeException("Could not find the top-left corner piece.");
    }

}

package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.constant.Edge;
import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.service.AssemblerService;
import com.devcom.puzzles.util.ImageConvertor;
import com.devcom.puzzles.util.MapUtil;
import com.devcom.puzzles.util.PuzzleEdgeMatcher;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AssemblerServiceImpl implements AssemblerService {

    Set<Location> assembledParts = new HashSet<>();

    @Override
    public List<PuzzleEntry> assemble(List<PuzzleEntry> entries) {
        assembledParts.clear();
        Map<Location, Mat> puzzles = ImageConvertor.convertToMatMap(entries);
//        int colsCount = puzzles.keySet()
//                .stream().map(Location::col)
//                .max(Comparator.comparingInt(c -> c))
//                .orElseThrow();
//

//        for (int i = 0; i <= colsCount; i++) {
//            for (Location location : puzzles.keySet()) {
//                if (Objects.equals(location.row(), 0) && Objects.equals(location.col(), i)) {
//                    assembleCol(location, puzzles);
//                    break;
//                }
//            }
//        }
        puzzles.keySet()
//                .filter(l -> Objects.equals(l.row(), 1))
                .forEach(l -> assembleCol(l, puzzles));

        return ImageConvertor.convertToPuzzleEntryList(puzzles);
    }

    private void assembleCol(Location location, Map<Location, Mat> puzzles) {
        if (assembledParts.contains(location)) {
            return;
        }

        Location nextLoc = recursiveAssembleDirection(location, puzzles, Edge.TOP);
        recursiveAssembleDirection(nextLoc, puzzles, Edge.BOTTOM);

    }

    private Location recursiveAssembleDirection(Location location, Map<Location, Mat> puzzles, Edge edge) {
        Location locationToSwap = PuzzleEdgeMatcher.getAdjacentPuzzlesLocation(location, puzzles, edge).orElse(null);
        if (locationToSwap != null) {
            return assembleStep(location, puzzles, edge, locationToSwap);
        } else return location;
    }

    private Location assembleStep(Location selected, Map<Location, Mat> puzzles, Edge edge, Location locationToSwap) {
        if (/*assembledParts.contains(selected) && works without exception*/ assembledParts.contains(locationToSwap)){
            return locationToSwap;
        }
        assembledParts.add(selected);

        Location swapped = null;
        Integer rows = puzzles.keySet().stream()
                .map(Location::row)
                .max(Comparator.comparingInt(r -> r))
                .orElse(0);

        if (isLocationOnBoardEdge(selected, edge, rows)) {
            Location dragged = dragAssembledCol(selected, puzzles, edge, locationToSwap);
            log.info("dragged {}",  dragged);
            if (locationToSwap.equals(dragged)) {
                recursiveAssembleDirection(selected, puzzles, edge);
            } else {
                swapped = swapParts(dragged, puzzles, locationToSwap, edge);
                recursiveAssembleDirection(swapped, puzzles, edge);
            }
            return dragged;
        } else {
            swapped = swapParts(selected, puzzles, locationToSwap, edge);
            log.info("swapped {}", swapped);
            recursiveAssembleDirection(swapped, puzzles, edge);
            return swapped;
        }
    }

    private Location dragAssembledCol(Location location, Map<Location, Mat> puzzles, Edge edge, Location locationToSwap) {
        Edge oppositeEdge = PuzzleEdgeMatcher.getOppositEdge(edge);
        List<Location> keysToMove = puzzles.keySet().stream()
                .filter(key -> isLocationAssembledAndInCurrentDirection(key, location, oppositeEdge))
                .collect(Collectors.toList());

        if (oppositeEdge.equals(Edge.BOTTOM)) {
            Collections.reverse(keysToMove);
        }
        for (Location key : keysToMove) {
            Location oppositeLocation = getOppositeLocation(oppositeEdge, key);
            assembledParts.remove(key);
            assembledParts.add(oppositeLocation);
            MapUtil.swapValues(puzzles, oppositeLocation, key);
            if (locationToSwap.equals(oppositeLocation)) {
                locationToSwap = key;
            }
        }
        return getOppositeLocation(oppositeEdge, location); //is the same?
//        return getOppositeLocation(oppositeEdge,  keysToMove.get(keysToMove.size() - 1));
    }

    private boolean isLocationAssembledAndInCurrentDirection(Location location, Location locationToCompare, Edge oppositEdge) {
        Predicate<Location> predicate = loc -> switch (oppositEdge) {
            case TOP, LEFT -> false;
            case BOTTOM -> loc.col() == locationToCompare.col();
            case RIGHT -> loc.row() == locationToCompare.row();
        };

        return predicate
                .and(assembledParts::contains)
                .test(location);
    }

    private boolean isLocationOnBoardEdge(Location location, Edge edge, int rows) {
        return switch (edge) {
            case LEFT -> location.col() == 0;
            case TOP -> location.row() == 0;
            case BOTTOM -> location.row() == rows;
            case RIGHT -> false;

        };
    }

    private Location swapParts(Location mainLocation, Map<Location, Mat> puzzles, Location locationToSwap, Edge edge) {
        Location oppositeLocation = getOppositeLocation(edge, mainLocation);
        assembledParts.add(oppositeLocation);
        if (oppositeLocation.equals(locationToSwap)) {
            return oppositeLocation;
        }
        MapUtil.swapValues(puzzles, locationToSwap, oppositeLocation);

        return oppositeLocation;
    }

    private Location getOppositeLocation(Edge edge, Location location) {
        int col = location.col();
        int row = location.row();

        return switch (edge) {
            case LEFT -> new Location(col - 1, row);
            case RIGHT -> new Location(col + 1, row);
            case TOP -> new Location(col, row - 1);
            case BOTTOM -> new Location(col, row + 1);
        };
    }
}

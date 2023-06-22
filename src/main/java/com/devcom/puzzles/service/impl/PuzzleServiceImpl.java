package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.dto.ImageSize;
import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.PuzzlesData;
import com.devcom.puzzles.service.PuzzleService;
import com.devcom.puzzles.util.ImageConvertor;
import com.devcom.puzzles.util.ImageSplitter;
import com.devcom.puzzles.util.MapUtil;
import lombok.Setter;
import lombok.SneakyThrows;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class PuzzleServiceImpl implements PuzzleService {
    @Setter
    private List<PuzzleEntry> snapshot;

    @Override
    public PuzzlesData processImageAndGetPuzzles(int rows, int cols) {
        Map<Location, Mat> puzzles = LinkedHashMap.newLinkedHashMap(rows * cols);
        ImageSize size = splitImageAndGetSize(rows, cols, puzzles);
        setSnapshot(ImageConvertor.convertToPuzzleEntryList(puzzles));

        MapUtil.shuffleMapValues(puzzles);


        List<PuzzleEntry> entries = ImageConvertor.convertToPuzzleEntryList(puzzles);
        return new PuzzlesData(size, entries);
    }

    @Override
    public boolean isCompleted(List<PuzzleEntry> puzzleEntries) {
        return comparePuzzleEntryLists(puzzleEntries, snapshot);
    }

    @SneakyThrows
    private ImageSize splitImageAndGetSize(int rows, int cols, Map<Location, Mat> puzzles) {
        File file = new File(getClass().getResource("/image3.png").toURI());
        return ImageSplitter.splitImage(file, rows, cols, puzzles);
    }


    private static boolean comparePuzzleEntryLists(List<PuzzleEntry> listToCompare,
                                                   List<PuzzleEntry> comparableList) {
        if (listToCompare == comparableList || listToCompare.size() != comparableList.size()) {
            return false;
        }

        for (int i = 0; i < listToCompare.size(); i++) {
            PuzzleEntry entry1 = listToCompare.get(i);
            PuzzleEntry entry2 = comparableList.get(i);

            if (!Objects.equals(entry1, entry2)) {
                return false;
            }
        }

        return true;
    }

}

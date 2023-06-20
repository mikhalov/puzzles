package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.service.PuzzleService;
import com.devcom.puzzles.dto.ImageSize;
import com.devcom.puzzles.dto.PuzzleEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import com.devcom.puzzles.dto.Location;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;
import com.devcom.puzzles.util.ImageSplitter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.devcom.puzzles.util.ImageConvertor.convertMatToBase64;

@Service
public class PuzzleServiceImpl implements PuzzleService {

    @Setter
    @Getter
    private List<PuzzleEntry> snapshot;

    @Override
    @SneakyThrows
    public ImageSize splitImageAndGetSize(int rows, int cols, Map<Location, Mat> puzzles) {
        File file = new File(getClass().getResource("/image.png").toURI());
        return ImageSplitter.splitImage(file, rows, cols, puzzles);
    }

    @Override
    public List<PuzzleEntry> convertToBase64(Map<Location, Mat> puzzles) {

        return puzzles.entrySet()
                .stream()
                .map(es -> new PuzzleEntry(es.getKey(), convertMatToBase64(es.getValue())))
                .toList();
    }

    @Override
    public boolean isCompleted(List<PuzzleEntry> puzzleEntries) {

        return comparePuzzleEntryLists(puzzleEntries, snapshot);
    }

    public static boolean comparePuzzleEntryLists(List<PuzzleEntry> list1, List<PuzzleEntry> list2) {
        if (list1 == list2) {
            return true;
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            PuzzleEntry entry1 = list1.get(i);
            PuzzleEntry entry2 = list2.get(i);

            if (!Objects.equals(entry1, entry2)) {
                return false;
            }
        }

        return true;
    }


}

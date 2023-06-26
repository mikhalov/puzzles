package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.dto.ImageSize;
import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.PuzzlesData;
import com.devcom.puzzles.model.Image;
import com.devcom.puzzles.service.ImageService;
import com.devcom.puzzles.service.PuzzleService;
import com.devcom.puzzles.util.ImageConvertor;
import com.devcom.puzzles.util.ImageSplitter;
import com.devcom.puzzles.util.MapUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PuzzleServiceImpl implements PuzzleService {
    private final ImageService imageService;
    @Setter
    private List<PuzzleEntry> snapshot;
    @Value("${puzzles.rows}")
    private int rows;
    @Value("${puzzles.cols}")
    private int cols;

    @Override
    public PuzzlesData processImageAndGetPuzzles(String imageId) {
        Map<Location, Mat> puzzles = LinkedHashMap.newLinkedHashMap(rows * cols);
        ImageSize size = splitImageAndGetSize(imageId, puzzles);
        setSnapshot(ImageConvertor.convertToPuzzleEntryList(puzzles));

        MapUtil.shuffleMapValues(puzzles);


        List<PuzzleEntry> entries = ImageConvertor.convertToPuzzleEntryList(puzzles);
        return new PuzzlesData(size, entries);
    }

    @Override
    public boolean isCompleted(List<PuzzleEntry> puzzleEntries) {
        return comparePuzzleEntryLists(puzzleEntries, snapshot);
    }

    private ImageSize splitImageAndGetSize(String imageId, Map<Location, Mat> puzzles) {
        Image image = imageService.getImageById(imageId);

        return ImageSplitter.splitImage(image.getBase64(), rows, cols, puzzles);
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

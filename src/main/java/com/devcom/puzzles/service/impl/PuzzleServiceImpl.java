package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.dto.ImageSize;
import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.request.PuzzleDataRequest;
import com.devcom.puzzles.dto.response.PuzzlesDataResponse;
import com.devcom.puzzles.model.GameSession;
import com.devcom.puzzles.model.Image;
import com.devcom.puzzles.service.GameSessionService;
import com.devcom.puzzles.service.ImageService;
import com.devcom.puzzles.service.PuzzleSessionService;
import com.devcom.puzzles.util.ImageConvertor;
import com.devcom.puzzles.util.ImageSplitter;
import com.devcom.puzzles.util.MapUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class PuzzleServiceImpl implements PuzzleSessionService {
    private final GameSessionService gameSessionService;
    private final ImageService imageService;
    private final Cache<String, List<PuzzleEntry>> snapshotCache = Caffeine.newBuilder()
            .maximumSize(50)
            .expireAfterAccess(1, TimeUnit.DAYS)
            .build();
    @Value("${puzzles.rows}")
    private int rows;
    @Value("${puzzles.cols}")
    private int cols;

    @Override
    public PuzzlesDataResponse processImageAndGetPuzzles(String imageId) {
        Map<Location, Mat> puzzles = LinkedHashMap.newLinkedHashMap(rows * cols);
        ImageSize size = splitImageAndGetSize(imageId, puzzles);
        String sessionId = gameSessionService.createSession(new GameSession(imageId));
        snapshotCache.put(sessionId, ImageConvertor.convertToPuzzleEntryList(puzzles));

        MapUtil.shuffleMapValues(puzzles);
        var entries = ImageConvertor.convertToPuzzleEntryList(puzzles);

        return new PuzzlesDataResponse(sessionId, size, entries);
    }

    @Override
    public boolean isCompleted(PuzzleDataRequest puzzleDataRequest) {
        GameSession session = gameSessionService.getSession(puzzleDataRequest.sessionId());
        List<PuzzleEntry> snapshot = snapshotCache.getIfPresent(session.getId());

        return comparePuzzleEntryLists(puzzleDataRequest.entries(), snapshot);
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

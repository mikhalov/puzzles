package com.devcom.puzzles.util;

import com.devcom.puzzles.dto.Location;
import com.devcom.puzzles.dto.PuzzleEntry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageConvertor {

    public static List<PuzzleEntry> convertToPuzzleEntryList(Map<Location, Mat> puzzles) {

        return puzzles.entrySet()
                .stream()
                .map(es -> new PuzzleEntry(
                        es.getKey(), ImageConvertor.convertMatToBase64(es.getValue())
                ))
                .toList();
    }

    public static Map<Location, Mat> convertToMatMap(List<PuzzleEntry> entries) {
        return entries.stream()
                .collect(Collectors.toMap(
                        PuzzleEntry::location,
                        entry -> ImageConvertor.convertBase64ToMat(entry.base64()),
                        (o, n) -> n,
                        LinkedHashMap::new
                ));
    }

    private static String convertMatToBase64(Mat image) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", image, matOfByte);
        byte[] imageBytes = matOfByte.toArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private static Mat convertBase64ToMat(String base64) {
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        MatOfByte matOfByte = new MatOfByte(imageBytes);

        return Imgcodecs.imdecode(matOfByte, Imgcodecs.IMREAD_UNCHANGED);

    }
}

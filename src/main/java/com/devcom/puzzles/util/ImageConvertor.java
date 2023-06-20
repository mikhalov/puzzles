package com.devcom.puzzles.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageConvertor {
    public static String convertMatToBase64(Mat image) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", image, matOfByte);
        byte[] imageBytes = matOfByte.toArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}

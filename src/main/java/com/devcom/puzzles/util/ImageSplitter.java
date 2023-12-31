package com.devcom.puzzles.util;

import com.devcom.puzzles.dto.ImageSize;
import com.devcom.puzzles.dto.Location;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageSplitter {

    public static ImageSize splitImage(String base64, int rows, int cols, Map<Location, Mat> pieces) {
        Mat image = ImageConvertor.convertBase64ToMat(base64);

        ImageSize imageSize = new ImageSize(image.height(), image.width());

        int puzzleWidth = image.cols() / cols;
        int puzzleHeight = image.rows() / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Rect region = new Rect(col * puzzleWidth, row * puzzleHeight, puzzleWidth, puzzleHeight);
                Mat imagePiece = new Mat(image, region);
                pieces.put(new Location(col, row), imagePiece);
            }
        }
        return imageSize;
    }
}
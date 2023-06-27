package com.devcom.puzzles.service;

import com.devcom.puzzles.dto.ImageRequest;
import com.devcom.puzzles.dto.ImageResponse;
import com.devcom.puzzles.model.Image;

import java.util.List;

public interface ImageService {

    ImageResponse createImage(ImageRequest imageRequest);
    List<Image> getImages();
    Image getImageById(String id);
}

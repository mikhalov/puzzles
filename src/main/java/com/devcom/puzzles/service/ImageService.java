package com.devcom.puzzles.service;

import com.devcom.puzzles.model.Image;

import java.util.List;

public interface ImageService {

    Image createImage(String base64);
    List<Image> getImages();
    Image getImageById(String id);
}

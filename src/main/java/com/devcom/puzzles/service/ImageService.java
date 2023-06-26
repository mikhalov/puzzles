package com.devcom.puzzles.service;

import com.devcom.puzzles.dto.ImageDTO;
import com.devcom.puzzles.model.Image;

import java.util.List;

public interface ImageService {

    Image createImage(ImageDTO imageDTO);
    List<Image> getImages();
    Image getImageById(String id);
}

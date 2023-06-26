package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.model.Image;
import com.devcom.puzzles.repository.ImageRepository;
import com.devcom.puzzles.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public Image createImage(String base64) {
        return imageRepository.save(new Image(base64));
    }

    @Override
    public List<Image> getImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image getImageById(String id) {
        return imageRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}

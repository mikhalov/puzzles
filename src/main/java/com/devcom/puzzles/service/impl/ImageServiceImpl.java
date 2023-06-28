package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.constant.Format;
import com.devcom.puzzles.dto.request.ImageRequest;
import com.devcom.puzzles.dto.response.ImageResponse;
import com.devcom.puzzles.model.Image;
import com.devcom.puzzles.repository.ImageRepository;
import com.devcom.puzzles.service.ImageService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    private final Cache<String, Image> imageCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    @Override
    public ImageResponse createImage(ImageRequest imageRequest) {
        Format format = Format.getFormat(imageRequest.mimeType().split("/")[1]);
        Image image = Image.builder()
                .base64(imageRequest.base64())
                .format(format)
                .build();
        Image savedImage = imageRepository.save(image);

        imageCache.put(savedImage.getId(), savedImage);

        return new ImageResponse(image);
    }

    @Override
    public List<Image> getImages() {
        List<Image> images = List.copyOf(imageCache.asMap().values());

        if (images.isEmpty()) {
            images = updateCache();
        }

        return images;
    }

    @Override
    public Image getImageById(String id) {
        return imageCache.get(id, key -> imageRepository.findById(key)
                .orElseThrow(NoSuchElementException::new));
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void clearAndUpdateCache() {
        try {
            imageCache.invalidateAll();
            log.info("Cache invalidated");
            updateCache();
            log.info("Cache updated");
        } catch (Exception e) {
            log.warn("Error occurred while clear and update cache", e);
        }

    }

    private List<Image> updateCache() {
        List<Image> images = imageRepository.findAll();
        images.forEach(image -> imageCache.put(image.getId(), image));

        return images;
    }

}

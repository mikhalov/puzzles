package com.devcom.puzzles.controller;

import com.devcom.puzzles.model.Image;
import com.devcom.puzzles.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;


    @GetMapping("/{id}")
    public ResponseEntity<Image> getImage(@PathVariable String id) {
        Image image = imageService.getImageById(id);

        return ResponseEntity.ok(image);
    }

    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.getImages());
    }

    @PostMapping
    public ResponseEntity<Image> addImage(@RequestBody String base64) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(imageService.createImage(base64));
    }


}

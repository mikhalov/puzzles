package com.devcom.puzzles.dto;

import com.devcom.puzzles.model.Image;

public record ImageResponse(String id, String base64, String mimeType) {

    public ImageResponse(Image image) {
        this(image.getId(), image.getBase64(), image.getFormat().getValue());
    }
}

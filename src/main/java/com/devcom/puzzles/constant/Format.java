package com.devcom.puzzles.constant;

import lombok.Getter;

@Getter
public enum Format {

    PNG("png"),
    JPEG("jpeg"),
    JPG("jpg");

    private final String value;

    Format(String value) {
        this.value = value;
    }

    public static Format getFormat(String format) {
        if (format == null) {
            throw new IllegalArgumentException("Format must not be null");
        }

        String normalizedFormat = format.toLowerCase();

        return switch (normalizedFormat) {
            case "png" -> PNG;
            case "jpg" -> JPG;
            case "jpeg" -> JPEG;
            default -> throw new IllegalArgumentException("Unexpected format: " + format);
        };
    }
}
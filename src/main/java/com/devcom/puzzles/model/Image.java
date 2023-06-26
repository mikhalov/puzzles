package com.devcom.puzzles.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "images")
public class Image {
    @Id
    private String id;
    private final String base64;
    private final String format;
}

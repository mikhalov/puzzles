package com.devcom.puzzles.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "gameSession")
@RequiredArgsConstructor
public class GameSession {
    private String id;
    private final String imageId;
}

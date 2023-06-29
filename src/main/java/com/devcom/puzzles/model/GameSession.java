package com.devcom.puzzles.model;

import com.devcom.puzzles.dto.PuzzleEntry;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@Document(collection = "gameSession")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameSession {
    private String id;
    private String imageId;
    private List<PuzzleEntry> snapshot;
}

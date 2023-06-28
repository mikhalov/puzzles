package com.devcom.puzzles.model;

import com.devcom.puzzles.constant.Format;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = "images")
public class Image {
    @Id
    private String id;
    private String base64;
    private Format format;
}

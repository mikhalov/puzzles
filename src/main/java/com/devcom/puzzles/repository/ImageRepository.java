package com.devcom.puzzles.repository;

import com.devcom.puzzles.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<Image, String> {
}

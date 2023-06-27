package com.devcom.puzzles.repository;

import com.devcom.puzzles.model.GameSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameSessionRepository extends MongoRepository<GameSession, String> {

}

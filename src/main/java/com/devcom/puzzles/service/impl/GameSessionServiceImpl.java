package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.model.GameSession;
import com.devcom.puzzles.repository.GameSessionRepository;
import com.devcom.puzzles.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {
    private final GameSessionRepository gameSessionRepository;

    @Override
    public String createSession(GameSession gameSession) {
        return gameSessionRepository.save(gameSession).getId();
    }

    @Override
    public GameSession getSession(String id) {
        return gameSessionRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}

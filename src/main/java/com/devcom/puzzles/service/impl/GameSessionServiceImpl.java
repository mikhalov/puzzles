package com.devcom.puzzles.service.impl;

import com.devcom.puzzles.model.GameSession;
import com.devcom.puzzles.repository.GameSessionRepository;
import com.devcom.puzzles.service.GameSessionService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {
    private final GameSessionRepository gameSessionRepository;
    private final Cache<String, GameSession> sessionCache = Caffeine.newBuilder()
            .maximumSize(50)
            .expireAfterAccess(1, TimeUnit.DAYS)
            .build();

    @Override
    public String createSession(GameSession gameSession) {
        GameSession saved = gameSessionRepository.save(gameSession);
        sessionCache.put(saved.getId(), saved);

        return saved.getId();
    }

    @Override
    public GameSession getSession(String id) {
        return sessionCache.get(id, key -> gameSessionRepository.findById(key)
                .orElseThrow(NoSuchElementException::new));

    }
}

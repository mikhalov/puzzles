package com.devcom.puzzles.service;


import com.devcom.puzzles.model.GameSession;

public interface GameSessionService {

    String createSession(GameSession gameSession);

    GameSession getSession(String id);
}

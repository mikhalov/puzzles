package com.devcom.puzzles.exception;

public class CannotAssemblePuzzleException extends RuntimeException {
    public CannotAssemblePuzzleException() {
    }

    public CannotAssemblePuzzleException(String message) {
        super(message);
    }
}

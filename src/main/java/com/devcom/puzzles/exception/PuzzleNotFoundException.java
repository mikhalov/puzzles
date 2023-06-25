package com.devcom.puzzles.exception;

public class PuzzleNotFoundException extends RuntimeException {
    public PuzzleNotFoundException() {
    }

    public PuzzleNotFoundException(String s) {
        super(s);
    }
}

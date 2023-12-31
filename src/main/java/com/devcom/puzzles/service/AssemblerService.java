package com.devcom.puzzles.service;

import com.devcom.puzzles.dto.PuzzleEntry;
import com.devcom.puzzles.dto.request.PuzzleDataRequest;
import com.devcom.puzzles.exception.CannotAssemblePuzzleException;

import java.util.List;

public interface AssemblerService {

    List<PuzzleEntry> assemblePuzzle(PuzzleDataRequest puzzleDataRequest) throws CannotAssemblePuzzleException;

}

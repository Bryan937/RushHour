package com.example.rushhour.utils;

import android.content.Context;

import com.example.rushhour.ViewModel.Records;

import java.util.ArrayList;
import java.util.List;

public class Challenge {
    /**
     * The Challenge class represents a specific puzzle challenge within the Rush Hour game.
     * It manages information about the challenge, such as its unique identifier, maximum record,
     * current moves, and the list of movable blocks within the challenge.
     */
    private final Records recordsManager = new Records();
    private final List<Block> listOfBlocks = new ArrayList<>();
    private final int maxRecord;
    private final String id;
    private int moves = 0;


    public Challenge(String id, int maxRecord) {
        this.id = id;
        this.maxRecord = maxRecord;
    }

    public void incrementMoves() {
        this.moves += 1;
    }

    public void decrementMoves() {
        if(this.moves != 0) this.moves -= 1;
    }

    public int getMoves() {
        return this.moves;
    }

    public int getMaxRecord() { return maxRecord; }

    public int getRecord(Context context) {
            return recordsManager.getRecord(context, id);
    }

    public void updateRecord(Context context, int moves) {
        recordsManager.updateRecord(context, id, moves);
    }

    public void resetMoves() {
        moves = 0;
    }

    public String getId() { return this.id; }

    public List<Block> getListOfBlocks() { return listOfBlocks; }

    public void resetBlocks() { listOfBlocks.clear(); }

    public void addBlock(Block block) { this.listOfBlocks.add(block); }
}

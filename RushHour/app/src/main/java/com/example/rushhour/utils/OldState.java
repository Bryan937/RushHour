package com.example.rushhour.utils;

public class OldState {
    /**
     * The OldState class represents the state of a Block at a previous moment in the game.
     * It stores the old position of the Block for potential undo functionality.
     */
    private final Position oldPosition;
    private final Block block;


    public OldState(Block block) {
        this.block = block;
        this.oldPosition = block.getPosition();
    }

    public Block getBlock() {
        return this.block;
    }

    public Position getBlockPosition() {
        return this.oldPosition;
    }
}

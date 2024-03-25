package com.example.rushhour.services;

import android.util.Log;

import com.example.rushhour.R;
import com.example.rushhour.constants.BlocksConstants;
import com.example.rushhour.utils.Block;
import com.example.rushhour.enums.Orientation;
import com.example.rushhour.utils.Challenge;
import com.example.rushhour.utils.OldState;
import com.example.rushhour.utils.Position;

import java.util.Stack;

public class ChallengeService {
    private final Stack<OldState> oldStatesStack = new Stack<>();
    private Challenge currentChallenge;


    /**
     * Sets the current challenge for the game.
     *
     * @param challenge The Challenge object to set as the current challenge.
     */
    public void setChallenge(Challenge challenge) { currentChallenge = challenge; }


    /**
     * Resets the state stack used for storing previous block states.
     * Clears all elements from the stack.
     */
    public void resetStateStack() {
        this.oldStatesStack.removeAllElements();
    }


    /**
     * Retrieves the current challenge associated with the game.
     *
     * @return The current Challenge object.
     */
    public Challenge getChallenge() { return currentChallenge; }


    /**
     * Saves the position of a block in the current challenge and increments the move count.
     *
     * @param block The Block object whose position is to be saved.
     */
    public void saveBlockPosition(Block block) {
        OldState state = new OldState(block);
        oldStatesStack.push(state);
        currentChallenge.incrementMoves();
    }


    /**
     * Steps back to the previous state of a block, updating its position and decrementing the move count.
     *
     * @return The Block object representing the previous state, or null if the state stack is empty.
     */
    public Block stepBack() {
        if (!oldStatesStack.empty()) {
            OldState state = oldStatesStack.pop();
            state.getBlock().setPosition(state.getBlockPosition());
            currentChallenge.decrementMoves();
            return state.getBlock();
        }

        return null;
    }


    /**
     * Creates and returns the first predefined challenge for the game.
     *
     * @return The first Challenge object with preconfigured blocks.
     */
    public Challenge createFirstChallenge() {
        Challenge firstChallenge = new Challenge("1", 15);
        firstChallenge.resetBlocks();

        firstChallenge.addBlock(new Block(R.drawable.horizontal3, 3, 1, new Position(0, 0), Orientation.HORIZONTAL));
        firstChallenge.addBlock(new Block(R.drawable.vertical3, 1, 3, new Position(1, 2), Orientation.VERTICAL));
        firstChallenge.addBlock(new Block(R.drawable.red_brick, BlocksConstants.MARKED_BLOCK_WIDTH, 1, BlocksConstants.MARKED_BLOCK_POSITION, Orientation.HORIZONTAL));
        firstChallenge.addBlock(new Block(R.drawable.vertical2, 1, 2, new Position(3, 0), Orientation.VERTICAL));
        firstChallenge.addBlock(new Block(R.drawable.horizontal3, 3, 1, new Position(5, 0), Orientation.HORIZONTAL));
        firstChallenge.addBlock(new Block(R.drawable.vertical3, 1, 3, new Position(0, 5), Orientation.VERTICAL));
        firstChallenge.addBlock(new Block(R.drawable.horizontal2, 2, 1, new Position(3, 4), Orientation.HORIZONTAL));
        firstChallenge.addBlock(new Block(R.drawable.vertical2, 1, 2, new Position(4, 4), Orientation.VERTICAL));

        return firstChallenge;
    }


    /**
     * Creates and returns the second predefined challenge for the game.
     *
     * @return The second Challenge object with preconfigured blocks.
     */
    public Challenge createSecondChallenge() {
        Challenge secondChallenge = new Challenge("2", 17);
        secondChallenge.resetBlocks();

        secondChallenge.addBlock(new Block(R.drawable.vertical2, 1, 2, new Position(1, 2), Orientation.VERTICAL));
        secondChallenge.addBlock(new Block(R.drawable.vertical3, 1, 3, new Position(1, 3), Orientation.VERTICAL));
        secondChallenge.addBlock(new Block(R.drawable.vertical3, 1, 3, new Position(1, 4), Orientation.VERTICAL));
        secondChallenge.addBlock(new Block(R.drawable.red_brick, BlocksConstants.MARKED_BLOCK_WIDTH, 1, BlocksConstants.MARKED_BLOCK_POSITION, Orientation.HORIZONTAL));
        secondChallenge.addBlock(new Block(R.drawable.horizontal2, 2, 1, new Position(3, 0), Orientation.HORIZONTAL));
        secondChallenge.addBlock(new Block(R.drawable.vertical2, 1, 2, new Position(3, 2), Orientation.VERTICAL));
        secondChallenge.addBlock(new Block(R.drawable.vertical2, 1, 2, new Position(4, 1), Orientation.VERTICAL));
        secondChallenge.addBlock(new Block(R.drawable.horizontal2, 2, 1, new Position(5, 2), Orientation.HORIZONTAL));

        return secondChallenge;
    }


    /**
     * Creates and returns the third predefined challenge for the game.
     *
     * @return The third Challenge object with preconfigured blocks.
     */
    public Challenge createThirdChallenge() {
        Challenge thirdChallenge = new Challenge("3", 15);
        thirdChallenge.resetBlocks();

        thirdChallenge.addBlock(new Block(R.drawable.vertical2, 1, 2, new Position(0, 0), Orientation.VERTICAL));
        thirdChallenge.addBlock(new Block(R.drawable.horizontal2, 2, 1, new Position(0, 1), Orientation.HORIZONTAL));
        thirdChallenge.addBlock(new Block(R.drawable.horizontal2, 2, 1, new Position(0, 3), Orientation.HORIZONTAL));
        thirdChallenge.addBlock(new Block(R.drawable.red_brick, BlocksConstants.MARKED_BLOCK_WIDTH, 1, BlocksConstants.MARKED_BLOCK_POSITION, Orientation.HORIZONTAL));
        thirdChallenge.addBlock(new Block(R.drawable.vertical2, 1, 2, new Position(1, 2), Orientation.VERTICAL));
        thirdChallenge.addBlock(new Block(R.drawable.vertical3, 1, 3, new Position(2, 3), Orientation.VERTICAL));
        thirdChallenge.addBlock(new Block(R.drawable.vertical3, 1, 3, new Position(2, 4), Orientation.VERTICAL));
        thirdChallenge.addBlock(new Block(R.drawable.horizontal3, 3, 1, new Position(4, 0), Orientation.HORIZONTAL));

        return thirdChallenge;
    }
}

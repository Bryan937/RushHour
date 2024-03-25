package com.example.rushhour.services;

import static com.example.rushhour.constants.BlocksConstants.CELL_SIZE;
import static com.example.rushhour.constants.BlocksConstants.ROW_COLUMN_NUMBER;
import static com.example.rushhour.enums.Orientation.HORIZONTAL;
import static com.example.rushhour.enums.Orientation.VERTICAL;
import static java.lang.Math.abs;
import static java.lang.Math.round;

import android.content.Context;
import android.graphics.Rect;
import android.util.EventLog;
import android.view.View;

import com.example.rushhour.databinding.FragmentGameViewBinding;
import com.example.rushhour.utils.Block;
import com.example.rushhour.utils.Challenge;
import com.example.rushhour.utils.Position;

import java.util.List;


public class GameService {
    private final ChallengeService challengeService = new ChallengeService();
    private FragmentGameViewBinding gameViewBinding;
    private final Context context;
    private float startX;
    private float startY;



    /**
     * Constructor for the GameService class.
     *
     * @param context The context associated with the GameService, typically the application context.
     */
    public GameService(Context context) {
        this.context = context;
    }

    public void setStartCoords(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }


    /**
     * Sets the game view binding for the GameService.
     *
     * @param gameView The FragmentGameViewBinding to be set as the game view binding.
     */
    public void setGameView(FragmentGameViewBinding gameView) {
       gameViewBinding = gameView;
    }


    /**
     * Retrieves the ChallengeService associated with the GameService.
     *
     * @return The ChallengeService object.
     */
    public ChallengeService getChallengeService() { return challengeService; }


    /**
     * Saves the state of a block by delegating the task to the associated ChallengeService.
     *
     * @param block The Block object whose state is to be saved.
     */
    public void saveBlockState(Block block) {
        challengeService.saveBlockPosition(block);
    }


    /**
     * Initializes a new game based on the provided challenge.
     *
     * @param challenge The Challenge object representing the new game's configuration.
     * @return The list of Block objects associated with the initialized game.
     */
    public List<Block> initializeGame(Challenge challenge) {
        challengeService.resetStateStack();
        challengeService.setChallenge(challenge);
        updateChallengeName(challenge.getId());
        challenge.resetMoves();
        updateChallengeMoves();
        showRecords(challengeService.getChallenge().getRecord(context), challengeService.getChallenge().getMaxRecord());
        setArrowVisibility();

        return challengeService.getChallenge().getListOfBlocks();
    }


    /**
     * Sets the visibility of the arrow buttons in the game view based on the current challenge.
     * Updates the visibility of the arrow buttons in the game view accordingly.
     */
    public void setArrowVisibility() {
        int leftArrowVisibility = challengeService.getChallenge().getId().equals("1") ? View.INVISIBLE : View.VISIBLE;
        int rightArrowVisibility = challengeService.getChallenge().getId().equals("3") ? View.INVISIBLE : View.VISIBLE;

        gameViewBinding.arrowRight.setVisibility(rightArrowVisibility);
        gameViewBinding.arrowLeft.setVisibility(leftArrowVisibility);
    }


    /**
     * Updates the displayed name of the current challenge in the game view.
     *
     * @param id The ID of the challenge to be displayed.
     */
    public void updateChallengeName(String id) {
        gameViewBinding.puzzleName.setText(id);
    }


    /**
     * Retrieves the current number of moves made in the challenge.
     *
     * @return The number of moves made in the current challenge.
     */
    public int getChallengeMoves() {
        return challengeService.getChallenge().getMoves();
    }


    /**
     * Updates the displayed count of moves made in the current challenge in the game view.
     */
    public void updateChallengeMoves() {
        gameViewBinding.movesCount.setText(String.valueOf(challengeService.getChallenge().getMoves()));
    }


    /**
     * Updates the records for the current challenge based on the number of moves made.
     *
     * @param moves The number of moves made in the current challenge.
     */
    public void updateRecords(int moves) {
        challengeService.getChallenge().updateRecord(context, moves);
    }


    /**
     * Displays the current and maximum records for the challenge in the game view.
     *
     * @param currentRecord The current record for the challenge.
     * @param maxRecord     The maximum record for the challenge.
     */
    public void showRecords(int currentRecord, int maxRecord) {
        String record;
        record = "Record: " + (currentRecord == 0 ? "--" : currentRecord) + "/" + maxRecord;
        gameViewBinding.recordText.setText(record);
    }


    /**
     * Handles a click on the right arrow button, navigating to the next challenge and initializing the game.
     *
     * @return The list of Block objects associated with the initialized game.
     */
    public List<Block> rightArrowClick() {
        Challenge nextChallenge = challengeService.getChallenge().getId().equals("1") ? challengeService.createSecondChallenge() : challengeService.createThirdChallenge();
        return initializeGame(nextChallenge);
    }


    /**
     * Handles a click on the left arrow button, navigating to the previous challenge and initializing the game.
     *
     * @return The list of Block objects associated with the initialized game.
     */
    public List<Block> leftArrowClick() {
        Challenge nextChallenge = challengeService.getChallenge().getId().equals("3") ? challengeService.createSecondChallenge() : challengeService.createFirstChallenge();
        return initializeGame(nextChallenge);
    }


    /**
     * Handles a click on the reset button, resetting the current challenge and initializing the game.
     *
     * @return The list of Block objects associated with the initialized game.
     */
    public List<Block> resetClick(){
        switch (challengeService.getChallenge().getId()) {
            case "3":
                return initializeGame(challengeService.createThirdChallenge());
            case "2":
                return initializeGame(challengeService.createSecondChallenge());
            default :
                return initializeGame(challengeService.createFirstChallenge());
        }
    }


    /**
     * Sets up and initializes the next game based on the current challenge.
     *
     * @return The list of Block objects associated with the initialized game.
     */
    public List<Block> setUpNextGame() {
        return challengeService.getChallenge().getId().equals("1") ? initializeGame(challengeService.createSecondChallenge()) : initializeGame(challengeService.createThirdChallenge());
    }


    /**
     * Checks if the bounding rectangles of two views intersect.
     *
     * @param temp      The list of Block objects representing other views.
     * @param imageView2 The second View for which intersection is checked.
     * @return True if the bounding rectangle of imageView2 intersects with any of the bounding rectangles in temp, false otherwise.
     */
    public  boolean doImagesTouch(List<Block> temp, View imageView2) {
        Rect bounds2 = new Rect();
        imageView2.getGlobalVisibleRect(bounds2);

        for (Block block : temp) {
            View imageView1 = block.getBlockView();
            Rect bounds1 = new Rect();
            imageView1.getGlobalVisibleRect(bounds1);
            if (Rect.intersects(bounds1, bounds2)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if a Block is touching the bounds of the game grid.
     *
     * @param block The Block object to check.
     * @return True if the Block is touching the bounds, false otherwise.
     */
    public  boolean isTouchingBounds(Block block) {
        float x = block.getBlockView().getX() / CELL_SIZE;
        float y = block.getBlockView().getY() / CELL_SIZE;
        return x < 0 || y < 0 || x + block.getWidth()  > ROW_COLUMN_NUMBER ||  y + block.getHeight()  > ROW_COLUMN_NUMBER;
    }


    /**
     * Sets the delta value based on the difference between the actual and start coordinates.
     * - Limits the delta to a maximum speed to prevent rapid movements.
     *
     * @param actualCoord The current coordinate.
     * @param startCoord  The starting coordinate.
     * @return The adjusted delta value.
     */
    public float setDelta(float actualCoord, float startCoord) {
        float delta = actualCoord - startCoord;
        float maxSpeed = 50;
        if (Math.abs(delta) > maxSpeed) {
            delta = maxSpeed * Math.signum(delta);
        }
        return  delta;
    }


    /**
     * Updates a block's size within the gridView according to it's respective new position
     *
     * @param block The block we previously moved.
     */
    public void handleActionUp(Block block) {
        Position finalP = new Position(round(abs(block.getBlockView().getX() / CELL_SIZE)), round(abs(block.getBlockView().getY() / CELL_SIZE)));
        if(block.getOrientation() == HORIZONTAL) {
            block.getBlockView().setX(finalP.getRowNumber() * CELL_SIZE);
        }
        if(block.getOrientation() == VERTICAL) {
            block.getBlockView().setY(finalP.getColumnNumber() * CELL_SIZE);
        }
    }


    /**
     * Indicates whether the red marked block is close to the exit
     *
     * @param block A block object within the grid.
     * @return a boolean.
     */
    public boolean isMarkedBlockUnblocked(Block block) {
        float x = round(block.getBlockView().getY() / CELL_SIZE);
        float y = round(block.getBlockView().getX() / CELL_SIZE);
        return  x == 2.0 && y == 4.0;
    }


    /**
     * Updates a horizontal block's position within its respective blockView after a touch event
     *
     * @param block The block we currently moved.
     * @param x  The starting coordinate.
     * @param list the list of other blocks within the grid.
     */
    public void moveHorizontalBlock(Block block, float x, List<Block> list) {
        float deltaX = setDelta(x, startX);
        float newX = block.getBlockView().getX() + deltaX;

        if (doImagesTouch(list, block.getBlockView()) || isTouchingBounds(block)) {
            return;
        }
        block.setPosition(new Position(block.getPosition().getRowNumber(), round(abs(newX) / CELL_SIZE)));
        block.getBlockView().setX(newX);
        startX = x;
    }



    /**
     * Updates a vertical block's position within its respective blockView after a touch event
     *
     * @param block The block we currently moved.
     * @param y  The starting coordinate.
     * @param list the list of other blocks within the grid.
     */
    public void moveVerticalBlock(Block block, float y, List<Block> list) {
        float deltaY = setDelta(y, startY);
        float newY = block.getBlockView().getY() + deltaY;
        if (doImagesTouch(list, block.getBlockView()) || isTouchingBounds(block)) {
            return;
        }
        block.setPosition(new Position(round(abs(newY) / CELL_SIZE), block.getPosition().getColumnNumber()));
        block.getBlockView().setY(newY);
        startY = y;
    }


    /**
     * Updates a block's position within the grid after a complete touch event
     *
     * @param block a block object.
     * @param startX  The starting x-axis coordinate.
     * @param startY the starting y-axis coordinate.
     * @param puzzleStatus a boolean indicating whether the current game was won.
     */
    public boolean handleActionUpEvent(Block block, float startX, float startY, boolean puzzleStatus) {
        handleActionUp(block);

        int initRow = round(abs(startX / CELL_SIZE));
        int initCol = round(abs(startY / CELL_SIZE));
        block.setPosition(new Position(initCol, initRow));

        int row = round(abs(block.getBlockView().getX()/ CELL_SIZE));
        int col = round(abs(block.getBlockView().getY()/ CELL_SIZE));

        if (initRow != row || initCol != col) {
            saveBlockState(block);
            updateChallengeMoves();
            if (puzzleStatus) return false;
        }

        return puzzleStatus;
    }


    /**
     * Resets a block within the gridView after undoing an action
     *
     */
    public void undoMove() {
        Block block = getChallengeService().stepBack();

        if (block != null) {
            float x = block.getPosition().getColumnNumber();
            float y = block.getPosition().getRowNumber();
            for (Block b : getChallengeService().getChallenge().getListOfBlocks()) {
                if (b == block) {
                    b.getBlockView().setX(x * CELL_SIZE);
                    b.getBlockView().setY(y * CELL_SIZE);
                }
            }
            updateChallengeMoves();
        }
    }

}

package com.example.rushhour.utils;

import android.view.View;
import com.example.rushhour.enums.Orientation;

public class Block {
    /**
     * The Block class represents a movable block within the Rush Hour game.
     * It encapsulates information about the block's orientation, size, position, and visual representation.
     */
    private final Orientation orientation;
    private final boolean isMarked;
    private final int drawableId;
    private Position position;
    public boolean touchFlag;
    private final int height;
    private final int width;
    private View blockView;


    public Block(int drawableId, int width, int height, Position position, Orientation orientation) {
        this.drawableId = drawableId;
        this.width = width;
        this.height = height;
        this.position = position;
        this.orientation = orientation;
        this.isMarked = position.equals(new Position(2, 0));
    }

    public int getDrawableId() { return drawableId; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Orientation getOrientation() { return orientation; }

    public Position getPosition() { return position; }

    public View getBlockView() { return this.blockView; }

    public boolean isMarked() { return isMarked; }

    public void setBlockView(View blockView) { this.blockView = blockView; }

    public void setPosition(Position position) { this.position = position; }
}

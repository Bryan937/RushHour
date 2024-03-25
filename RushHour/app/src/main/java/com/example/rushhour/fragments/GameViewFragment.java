package com.example.rushhour.fragments;

import static com.example.rushhour.constants.BlocksConstants.CELL_SIZE;
import static com.example.rushhour.constants.BlocksConstants.GRID_WIDTH;
import static com.example.rushhour.enums.Orientation.HORIZONTAL;
import static com.example.rushhour.enums.Orientation.VERTICAL;
import static java.lang.Math.abs;
import static java.lang.Math.round;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.rushhour.R;
import com.example.rushhour.ViewModel.Records;
import com.example.rushhour.constants.BlocksConstants;
import com.example.rushhour.databinding.FragmentGameViewBinding;
import com.example.rushhour.services.GameService;
import com.example.rushhour.utils.Block;
import com.example.rushhour.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class GameViewFragment extends Fragment {
    private final Records recordsManager = new Records();
    private FragmentGameViewBinding gameViewBinding;
    private boolean isPuzzleDone = false;
    private GameService gameService;
    private LayoutInflater inflater;
    private GridLayout gridLayout;
    private float initStartX;
    private float initStartY;


    /**
     * Creates and configures the view associated with the fragment.
     * Initializes the game with an initial challenge after the grid layout is complete.
     * Returns the root view of the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gameViewBinding = FragmentGameViewBinding.inflate(inflater, container, false);
        gridLayout = gameViewBinding.gridLayout;
        this.inflater = LayoutInflater.from(requireContext());

        gameService = new GameService(requireContext());
        gameService.setGameView(gameViewBinding);
        recordsManager.createRecordCSV(requireContext());

        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                List<Block> listOfBlocks = gameService.initializeGame(gameService.getChallengeService().createFirstChallenge());
                updateGridLayout(listOfBlocks);
            }
        });

        return gameViewBinding.getRoot();
    }


    /**
     * onViewCreated method Configures click listeners for arrow buttons and other UI elements.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameViewBinding.arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Block> blocks = gameService.rightArrowClick();
                updateGridLayout(blocks);
            }
        });

        gameViewBinding.arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Block> blocks = gameService.leftArrowClick();
                updateGridLayout(blocks);
            }
        });

        gameViewBinding.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(GameViewFragment.this)
                        .navigate(R.id.action_gameViewFragment_to_MenuFragment);
            }
        });

        gameViewBinding.redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gameService.undoMove();
            }
        });

        gameViewBinding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Block> blocks = gameService.resetClick();
                updateGridLayout(blocks);
            }
        });
    }


    /**
     * Updates the grid layout based on the provided list of blocks.
     *
     * @param blocks List of Block objects representing the current state of the grid.
     */
    private void updateGridLayout(List<Block> blocks) {
        gridLayout.removeAllViews();

        for (Block block : blocks) {
            View blockView = createVisualizedBlock(block, inflater);
            setupBlockTouchListener(blockView, block, blocks);
        }
    }


    /**
     * Sets up a touch listener for the given block view, triggering the handling of touch events.
     *
     * @param blockView The visual representation of the block.
     * @param block     The Block object associated with the block view.
     * @param blocks    List of Block objects representing the current state of the grid.
     */
    private void setupBlockTouchListener(View blockView, Block block, List<Block> blocks) {
        blockView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleBlockTouch(block, blocks, blockView, event);
                return true;
            }
        });
    }


    /**
     * Handles touch events for the given block, updating its position and performing related actions.
     *
     * @param block      The Block object associated with the touch event.
     * @param blocks     List of Block objects representing the current state of the grid.
     * @param blockView  The visual representation of the block.
     * @param event      The MotionEvent that triggered the touch event.
     */
    private void handleBlockTouch(Block block, List<Block> blocks, View blockView, MotionEvent event) {
        block.touchFlag = false;
        List<Block> temp = new ArrayList<>(blocks);
        temp.remove(block);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gameService.setStartCoords(event.getX(), event.getY());
                initStartX = blockView.getX();
                initStartY = blockView.getY();
                block.setBlockView(blockView);
                break;

            case MotionEvent.ACTION_MOVE:
                if (block.getOrientation() == HORIZONTAL) {
                    gameService.moveHorizontalBlock(block, event.getX(), temp);

                } else if (block.getOrientation() == VERTICAL) {
                    gameService.moveVerticalBlock(block, event.getY(), temp);
                }
                break;

            case MotionEvent.ACTION_UP:
                isPuzzleDone = gameService.handleActionUpEvent(block, initStartX, initStartY, isPuzzleDone);

                if (block.isMarked() && gameService.isMarkedBlockUnblocked(block)) {
                    final Handler handler = new Handler();
                    final Runnable moveBlockRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (blockView.getX() < GRID_WIDTH) {
                                blockView.setX(blockView.getX() + 10);
                                handler.postDelayed(this, 30);
                            }
                            else {
                                if (!isPuzzleDone) {
                                    isPuzzleDone = true;
                                    gameService.updateRecords(gameService.getChallengeMoves());
                                    showPuzzleSolvedDialog();
                                }
                            }
                        }
                    };
                    handler.post(moveBlockRunnable);
                }

                break;
        }
    }


    /**
     * Creates and visualizes the UI representation of the given block.
     *
     * @param block     The Block object for which to create the visual representation.
     * @param inflater  The LayoutInflater to inflate the block layout.
     * @return The View representing the visualized block.
     */
    public View createVisualizedBlock(Block block, LayoutInflater inflater) {
        gridLayout.removeView(block.getBlockView());

        View blockView = inflater.inflate(R.layout.block, null);
        ImageView imageView = blockView.findViewById(R.id.blockImage);
        imageView.setImageResource(block.getDrawableId());

        GridLayout.LayoutParams layoutParams = setupLayoutParams(block);
        blockView.setLayoutParams(layoutParams);

        gridLayout.addView(blockView);
        block.setBlockView(blockView);

        return blockView;
    }


    /**
     * Sets up GridLayout.LayoutParams for the given block based on its position and dimensions.
     *
     * @param block The Block object for which to set up layout parameters.
     * @return GridLayout.LayoutParams configured for the block.
     */
    public GridLayout.LayoutParams setupLayoutParams(Block block) {
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = gridLayout.getWidth() / 6 * block.getWidth();
        layoutParams.height = gridLayout.getHeight() / 6 * block.getHeight();
        layoutParams.rowSpec = GridLayout.spec((int) block.getPosition().getRowNumber(), block.getHeight());
        layoutParams.columnSpec = GridLayout.spec((int) block.getPosition().getColumnNumber(), block.getWidth());

        return layoutParams;
    }


    /**
     * Displays a dialog indicating successful completion of the puzzle.
     * Plays a success sound, shows a success window, and triggers subsequent actions.
     */
    private void showPuzzleSolvedDialog() {
        MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.success_sound);
        mediaPlayer.start();

        Dialog puzzleSolvedDialog = new Dialog(requireContext());
        puzzleSolvedDialog.setContentView(R.layout.window_succes);
        puzzleSolvedDialog.setCanceledOnTouchOutside(false);
        puzzleSolvedDialog.show();


        new Handler().postDelayed(() -> {
            AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            fadeOut.setDuration(50);

            View contentView = puzzleSolvedDialog.findViewById(android.R.id.content);
            contentView.startAnimation(fadeOut);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    puzzleSolvedDialog.dismiss();
                    isPuzzleDone = false;
                    List<Block> blocks = gameService.setUpNextGame();
                    updateGridLayout(blocks);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }, 3000);
    }

}
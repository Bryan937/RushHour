package com.example.rushhour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.rushhour.services.ChallengeService;
import com.example.rushhour.utils.Challenge;
import com.example.rushhour.utils.Position;

import org.junit.Test;

public class ChallengeUnitTest {
//    ChallengeService cService = new ChallengeService();
//    Challenge challenge1 = cService.createFirstChallenge();
//    @Test
//    public void createFirstChallengeShouldWork(){
//        assertEquals(8, challenge1.getListOfBlocks().size());
//    }
//
//    @Test
//    public void firstChallengeShouldHaveAMarkedBlock(){
//        HorizontalBlock bTest = (HorizontalBlock)challenge1.getBlock(0);
//        assertTrue(bTest.getIsMarked());
//    }
//    @Test
//    public void movingABlockShouldChangeBlockPosition(){
//        final Position p = new Position(2,5);
//        cService.moveBlock(challenge1, 0, new Position(2, 5));
//        HorizontalBlock bTest = (HorizontalBlock)challenge1.getBlock(0);
//        assertEquals(p, bTest.getPosition());
//    }
//    @Test
//    public void movingABlockShouldIncrementChallengeMoves(){
//        final Position p = new Position(2,5);
//        cService.moveBlock(challenge1, 0, new Position(2, 5));
//        HorizontalBlock bTest = (HorizontalBlock)challenge1.getBlock(0);
//        assertEquals(1, challenge1.getMoves());
//    }
//    @Test
//    public void movingABlockShouldAddOldPositionInTheStack(){
//        cService.moveBlock(challenge1, 0, new Position(2, 5));
//        assertEquals(1, cService.getOldStatesStack().size());
//    }
//    @Test
//    public void steppingBackShouldPutLastMovedBlockInHisLatestPosition(){
//        final Position p = challenge1.getBlock(1).getPosition();
//        cService.moveBlock(challenge1, 0, new Position(2, 5));
//        cService.moveBlock(challenge1, 1, new Position(0, 4));
//        HorizontalBlock bTest1 = (HorizontalBlock)challenge1.getBlock(1);
//        cService.stepBack(challenge1);
//        assertEquals(p, bTest1.getPosition());
//    }
//    @Test
//    public void steppingFullyShouldEmptyTheStack(){
//
//        final Position p = new Position(2,0);
//        cService.moveBlock(challenge1, 0, new Position(2, 5));
//        cService.moveBlock(challenge1, 1, new Position(0, 4));
//        HorizontalBlock bTest1 = (HorizontalBlock)challenge1.getBlock(0);
//        HorizontalBlock bTest2 = (HorizontalBlock)challenge1.getBlock(1);
//        cService.stepBack(challenge1);
//        cService.stepBack(challenge1);
//        assertEquals(0, cService.getOldStatesStack().size());
//    }
}

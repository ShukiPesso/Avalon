package Avalon;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MissionTest {

    @Test
    public void getMissionResultsSuccess() {
        Game g = GameTest.getGame();
        List<Integer> allowedKingts = new ArrayList<>();
        int i=0;

        int numOfKights = 3;
        for (;i<numOfKights;i++)
            allowedKingts.add(i);
        Mission m = new Mission(g.members,allowedKingts, numOfKights,false);
        for (;i < g.numOfPlayers ;i++)
            assertFalse(m.setMission(i,MissionCard.Success));
        i=0;
        for (;i<numOfKights;i++){
            assertEquals(MissionCard.WaitingForMoreKights,m.getMissionResults());
            assertTrue(m.setMission(i,MissionCard.Success));
        }
        assertEquals(MissionCard.Success,m.getMissionResults());
    }

    @Test
    public void getMissionResultsRegularFail() {
        Game g = GameTest.getGame();
        List<Integer> allowedKingts = new ArrayList<>();
        int i=0;

        int numOfKights = 3;
        for (;i<numOfKights;i++)
            allowedKingts.add(i);
        Mission m = new Mission(g.members,allowedKingts, numOfKights,false);
        for (;i < g.numOfPlayers ;i++)
            assertFalse(m.setMission(i,MissionCard.Fail));
        i=1;
        m.setMission(0,MissionCard.Fail);
        for (;i<numOfKights;i++){
            assertEquals(MissionCard.WaitingForMoreKights,m.getMissionResults());
            assertTrue(m.setMission(i,MissionCard.Success));
        }
        assertEquals(MissionCard.Fail,m.getMissionResults());
    }

    @Test
    public void getMissionResultsSpecialSuccess() {
        Game g;
        do{
            g = GameTest.getGame();
        } while (g.numOfPlayers > 8);

        List<Integer> allowedKingts = new ArrayList<>();
        int i=0;

        int numOfKights = GameTest.random.nextInt(3)+3;
        for (;i<numOfKights;i++)
            allowedKingts.add(i);
        Mission m = new Mission(g.members,allowedKingts, numOfKights,true);
        for (;i < g.numOfPlayers ;i++)
            assertFalse(m.setMission(i,MissionCard.Fail));
        i=1;
        m.setMission(0,MissionCard.Fail);
        for (;i<numOfKights;i++){
            assertEquals(MissionCard.WaitingForMoreKights,m.getMissionResults());
            assertTrue(m.setMission(i,MissionCard.Success));
        }
        assertEquals(MissionCard.Success,m.getMissionResults());
    }

    @Test
    public void getMissionResultsSpecialFail() {
        Game g;
        do{
            g = GameTest.getGame();
        } while (g.numOfPlayers > 8);

        List<Integer> allowedKingts = new ArrayList<>();
        int i=0;

        int numOfKights = GameTest.random.nextInt(3)+3;
        for (;i<numOfKights;i++)
            allowedKingts.add(i);
        Mission m = new Mission(g.members,allowedKingts, numOfKights,true);
        for (;i < g.numOfPlayers ;i++)
            assertFalse(m.setMission(i,MissionCard.Fail));
        i=0;
        for (;i<2;i++)
            m.setMission(i,MissionCard.Fail);
        for (;i<numOfKights;i++){
            assertEquals(MissionCard.WaitingForMoreKights,m.getMissionResults());
            assertTrue(m.setMission(i,MissionCard.Success));
        }
        assertEquals(MissionCard.Fail,m.getMissionResults());
    }

    @Test
    public void setMission() {

    }
}
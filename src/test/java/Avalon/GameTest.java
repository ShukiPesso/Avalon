package Avalon;

import org.junit.Test;
import com.pholser.junit.quickcheck.*;

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;


public class GameTest {
    static Random random = new Random(LocalTime.now().toSecondOfDay());
    @Test
    public void HappyPath() {

        int numOfPlayers = random.nextInt(6)+5;
        Game g = new Game(numOfPlayers);
        assertEquals(numOfPlayers, g.cards.size());
        for (int i=0; i<numOfPlayers ;i ++)
        {
            String name = RandomString.getAlphaNumericString(10);
            int id = g.setNewMember(name);
            assertEquals(numOfPlayers-i-1,g.cards.size());
            member m = g.getMember(id);
            assertEquals(name, m.name);
            assertEquals(id, m.id);
        }

        //try to add one more memebr, should Failed
        String name = RandomString.getAlphaNumericString(10);
        assertEquals(-1,g.setNewMember(name));
        assertEquals(1,g.round);
        assertEquals(1, g.RoundStack.size());
        List<Integer> SuggestList = setListForMission(g,g.RoundStack.peek().getNumOfKnights());
        assertTrue(g.suggestsKightsForMission(g.kingId,SuggestList));
        AllVoteTheSame(g,missionVote.Approve);
        assertFalse(g.suggestsKightsForMission(g.kingId,SuggestList));
        AllDoingTheSameCard(g,SuggestList,MissionCard.Success);
        assertEquals(2,g.round);
        RoundFailByVotes(g);
        assertEquals(3,g.round);
        RunRound(g,MissionCard.Success);
        assertEquals(4,g.round);
        RunRound(g,MissionCard.Fail);
        assertEquals(5,g.round);
        RunRound(g,MissionCard.Success);

    }

    void RoundFailByVotes(Game g){
        int roundNumber = g.round;
        List<Integer> SuggestList = setListForMission(g,g.RoundStack.peek().getNumOfKnights());
        for (int i=0;i<5;i++){
            assertTrue(g.suggestsKightsForMission(increaseKingId(g,i),SuggestList));
            AllVoteTheSame(g,missionVote.Reject);
        }
        if (roundNumber!=5)
            assertEquals(roundNumber+1,g.round);
    }

    int increaseKingId(Game g, int n){
        return (g.kingId+n)%g.numOfPlayers;
    }

    void RunRound(Game g, MissionCard card){
        int roundNumber = g.round;
        List<Integer> SuggestList = setListForMission(g,g.RoundStack.peek().getNumOfKnights());
        assertTrue(g.suggestsKightsForMission(g.kingId,SuggestList));
        AllVoteTheSame(g,missionVote.Approve);
        if (card==MissionCard.Success){
            AllDoingTheSameCard(g,SuggestList,card);
        }else{
            nDoingCardAllotherTheOposit(g,SuggestList,MissionCard.Fail,2);
        }
        if (roundNumber!=5)
            assertEquals(roundNumber+1,g.round);
    }

    List<Integer> setListForMission(Game g,int numOfKnights){
        Set<Integer> ids = new HashSet<>();
        for (int i=0;i<numOfKnights;i++){
            int newKnight;
            do{
                newKnight = random.nextInt(g.numOfPlayers);
            } while(ids.contains(newKnight));
            ids.add(newKnight);
        }
        List<Integer> l = new ArrayList<>(ids);
        return l;
    }
    void AllVoteTheSame(Game g, missionVote vote){
        for (int i=0; i<g.numOfPlayers;i++){
            g.vote(i,vote);
        }
    }

    void nVoteAllDitherTheOposit(Game g,missionVote vote, int n){
        int i=0;
        for(;i<n;i++){
            g.vote(i,vote);
        }
        missionVote opositeVote = vote==missionVote.Approve ? missionVote.Reject : missionVote.Approve;
        for (;i<g.numOfPlayers;i++){
            g.vote(i,opositeVote);
        }
    }

    void AllDoingTheSameCard(Game g,List<Integer> knights, MissionCard card){
        for (Integer id : knights){
            g.mission(id,card);
        }
    }

    void nDoingCardAllotherTheOposit(Game g,List<Integer> knights, MissionCard card, int n){
        MissionCard opositeCard = card==MissionCard.Success ? MissionCard.Fail : MissionCard.Success;
        List<Integer> firstList = knights.subList(0,n);
        List<Integer> secondList = knights.subList(n,knights.size());
        AllDoingTheSameCard(g,firstList,card);
        AllDoingTheSameCard(g,secondList,opositeCard);
    }

    static public Game getGame() {
        return getGame(null);
    }

    static public Game getGame(Integer NumOfPlayers)
    {
        int numOfPlayers;
        if (NumOfPlayers == null)
            numOfPlayers = random.nextInt(6)+5;
        else
            numOfPlayers = NumOfPlayers;
        Game g = new Game(numOfPlayers);
        fullfillGame(g);
        return g;
    }
    static public void fullfillGame (Game g)
    {
        for (int i=0; i<g.numOfPlayers ;i ++)
        {
            String name = RandomString.getAlphaNumericString(10);
            g.setNewMember(name);
        }
        //g.getNextKing();
        g.kingId=0;
    }
    @Test
    public void CheckRandomallyOfCards() throws InterruptedException {
        for (int i=0;i<100;i++) {
            int numOfPlayers = random.nextInt(6) + 5;
            Game g = new Game(numOfPlayers);
            Thread.sleep(10);
            Game g2 = new Game(numOfPlayers);
            assertNotEquals(g.cards,g2.cards);
        }

    }


    @Test
    public void getNextKing() {
        int numOfPlayers = random.nextInt(6) + 5;
        Game g = new Game(numOfPlayers);
        fullfillGame(g);
        for (int i=0;i<100;i++)
        {
            assertTrue(g.getNextKing() <= numOfPlayers);
            assertTrue(g.getNextKing() >= 0);
        }
    }
}
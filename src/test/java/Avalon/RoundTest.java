package Avalon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

public class RoundTest {

    Game g;
    Round r;
    List<Integer> list;
    Random random;
    @Before
    public void setUp() throws Exception {
        random = new Random(LocalTime.now().toNanoOfDay());
    }

    @After
    public void tearDown() throws Exception {
        g=null;
        r=null;
    }

    public void fullfillList (int n){
        Set<Integer> ids = new HashSet<>();
        while (ids.size() != n)
        {
            int id=random.nextInt(n)+1;
            if (!ids.contains(id)){
                ids.add(id);
            }
        }
        list = new ArrayList<>(ids);
    }

    @Test
    public void setNewVoting() {
        g = GameTest.getGame(5);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        fullfillList(3);
        assertFalse(r.setNewVoting(list,(g.kingId-1)%r.numOfPlayers));
        assertFalse(r.setNewVoting(list,g.kingId));
        fullfillList(2);
        assertTrue(r.setNewVoting(list,g.kingId));
        assertFalse(r.setNewVoting(list,g.kingId));
        r.Vote(0,missionVote.Reject);
        assertFalse(r.setNewVoting(list,g.kingId));
        assertFalse(r.Vote(0,missionVote.Reject));
        r.Vote(1,missionVote.Reject);
        r.Vote(2,missionVote.Reject);
        assertFalse(r.setNewVoting(list,g.kingId));

    }

    public void AllVoteTheSame (Round r, missionVote vote){
        int KingId = r.KingId;
        for (int i=0; i< r.numOfPlayers ; i++){
            r.Vote(i,vote);
        }
        assertEquals(vote,r.getVoteResults());
    }

    @Test
    public void voteRejectAtTheEnd() {
        g = GameTest.getGame(5);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        fullfillList(2);
        assertTrue(r.setNewVoting(list,r.KingId));
        for (Integer id : list)
        {
            r.Vote(id,missionVote.Approve);
            assertEquals(missionVote.WaitingForMoreVoting,r.getVoteResults());
        }
        for (int i=0; i< g.numOfPlayers; i++)
        {
            assertEquals(missionVote.WaitingForMoreVoting,r.getVoteResults());
            if (!list.contains(i))
                r.Vote(i,missionVote.Reject);
        }
        assertEquals(missionVote.Reject,r.getVoteResults());
        assertEquals(g.kingId+1,r.KingId);
    }

    @Test
    public void voteAllReject() {
        g = GameTest.getGame(5);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        fullfillList(2);
        assertTrue(r.setNewVoting(list,r.KingId));
        AllVoteTheSame(r,missionVote.Reject);
        assertEquals(g.kingId+1,r.KingId);
    }

    @Test
    public void voteAllAprove() {
        g = GameTest.getGame(5);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        fullfillList(2);
        assertTrue(r.setNewVoting(list,r.KingId));
        AllVoteTheSame(r,missionVote.Approve);
    }

    static public int nextKing(int kingId, int numOfPlayers){
        return (kingId+1)%numOfPlayers;
    }
    @Test
    public void RoundFailedInFiveVotes(){
        g = GameTest.getGame(7);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        fullfillList(2);
        for (int i=0;i<5;i++){
            assertTrue(r.setNewVoting(list,r.KingId));
            AllVoteTheSame(r,missionVote.Reject);
            if (i!=4)
                assertEquals(nextKing(g.kingId+i,g.numOfPlayers),r.KingId);
        }
        assertEquals(MissionCard.Fail,r.getRoundResult());
    }

    @Test
    public void goToMissionFailedBecauseNotApproved() {
        g = GameTest.getGame(7);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        assertFalse(r.goToMission(r.KingId,MissionCard.Success));
    }

    @Test
    public void goToMissionNotApprovedKnightFail() {
        g = GameTest.getGame(7);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        fullfillList(2);
        assertTrue(r.setNewVoting(list,r.KingId));
       AllVoteTheSame(r,missionVote.Approve);
       int id=0;
       while (list.contains(id))
           id++;
       assertFalse(r.goToMission(id,MissionCard.Fail));
    }

    @Test
    public void goToMissionSuccess() {
        g = GameTest.getGame(7);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        fullfillList(2);
        assertTrue(r.setNewVoting(list,r.KingId));
        AllVoteTheSame(r,missionVote.Approve);
        for (Integer id :list){
            assertTrue(r.goToMission(id,MissionCard.Success));
            assertFalse(r.goToMission(id,MissionCard.Success));
        }
        assertEquals(MissionCard.Success, r.getRoundResult());
    }

    @Test
    public void goToMissionFail() {
        g = GameTest.getGame(7);
        r = new Round(g.members,1,g.numOfPlayers,g.kingId);
        fullfillList(2);
        assertTrue(r.setNewVoting(list,r.KingId));
        AllVoteTheSame(r,missionVote.Approve);
        assertTrue(r.goToMission(list.get(0),MissionCard.Success));
        assertTrue(r.goToMission(list.get(1),MissionCard.Fail));
        assertEquals(MissionCard.Fail, r.getRoundResult());
    }

    @Test
    public void goToSpecialMissionFail() {
        g = GameTest.getGame(7);
        r = new Round(g.members,4,g.numOfPlayers,g.kingId);
        fullfillList(4);
        assertTrue(r.setNewVoting(list,r.KingId));
        AllVoteTheSame(r,missionVote.Approve);
        int i=0;
        for (; i<2; i++)
        {
            assertTrue(r.goToMission(list.get(i),MissionCard.Fail));
        }
        for (; i<list.size();i++){
            assertTrue(r.goToMission(list.get(i),MissionCard.Success));
        }
        assertEquals(MissionCard.Fail, r.getRoundResult());
    }

    @Test
    public void goToSpecialMissionSuccess() {
        g = GameTest.getGame(7);
        r = new Round(g.members,4,g.numOfPlayers,g.kingId);
        fullfillList(4);
        assertTrue(r.setNewVoting(list,r.KingId));
        AllVoteTheSame(r,missionVote.Approve);
        int i=0;
        for (; i<1; i++)
        {
            assertTrue(r.goToMission(list.get(i),MissionCard.Fail));
        }
        for (; i<list.size();i++){
            assertTrue(r.goToMission(list.get(i),MissionCard.Success));
        }
        assertEquals(MissionCard.Success, r.getRoundResult());
    }

}
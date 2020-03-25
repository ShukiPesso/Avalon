package Avalon;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class VotingTest {

    @Test
    public void setVoteFirstApprove() {

        Game g = GameTest.getGame();
        List<Integer> Knights = new ArrayList<>();
        for (int j=0; j<3 ; j++)
            Knights.add(j);
        Voting v  = new Voting(g.members,Knights,g.numOfPlayers,0);
        int majority = g.numOfPlayers/2 +1;
        int i=0;
        for (; i< majority; i++){
            assertEquals(missionVote.WaitingForMoreVoting,v.getVotingResults());
            v.setVote(i,missionVote.Approve);
        }
        for (; i< g.numOfPlayers ; i++) {
            assertEquals(missionVote.Approve, v.getVotingResults());
            v.setVote(i,missionVote.Reject);
        }
    }

    @Test
    public void setVoteFirstReject() {

        Game g = GameTest.getGame();
        List<Integer> Knights = new ArrayList<>();
        for (int j=0; j<3 ; j++)
            Knights.add(j);
        Voting v  = new Voting(g.members,Knights,g.numOfPlayers,1);
        int majority = g.numOfPlayers/2 +1;
        int i=0;
        for (; i< majority; i++){
            assertEquals(missionVote.WaitingForMoreVoting,v.getVotingResults());
            v.setVote(i,missionVote.Reject);
        }
        for (; i< g.numOfPlayers ; i++) {
            assertEquals(missionVote.Reject, v.getVotingResults());
            v.setVote(i,missionVote.Approve);
        }
    }

    @Test
    public void setVoteRejectWhenEqual() {
        Game g;
        do {
            g = GameTest.getGame();
        } while (g.numOfPlayers%2==0);
        List<Integer> Knights = new ArrayList<>();
        for (int j=0; j<3 ; j++)
            Knights.add(j);
        Voting v  = new Voting(g.members,Knights,g.numOfPlayers,3);
        int equal = g.numOfPlayers/2;
        int i=0;
        for (; i< equal; i++){
            assertEquals(missionVote.WaitingForMoreVoting,v.getVotingResults());
            v.setVote(i,missionVote.Approve);
        }
        for (; i< g.numOfPlayers ; i++) {
            assertEquals(missionVote.WaitingForMoreVoting, v.getVotingResults());
            v.setVote(i,missionVote.Reject);
        }
        assertEquals(missionVote.Reject, v.getVotingResults());
    }
}
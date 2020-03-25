package Avalon;

import java.text.MessageFormat;
import java.util.*;

public class Round {
    int numOfKights;
    int RoundNumber;
    int numOfPlayers;
    int KingId;
    HashMap<Integer,member> members;
    Stack<Voting> votingStack;
    Mission mission;
    boolean isSpecialRound;
    MissionCard RoundResult;
    int[][] numOfKnightPerQuestTable = {{2,2,2,3,3,3},{3,3,3,4,4,4},{2,4,3,4,4,4},{3,3,4,5,5,5},{3,4,4,5,5,5}};

    public Round(HashMap<Integer,member> _members, int _roundNumber, int _numOfPlayers, int _king)
    {
        members = _members;
        RoundNumber = _roundNumber;
        numOfPlayers = _numOfPlayers;
        KingId = _king;
        isSpecialRound = RoundNumber == 4 && numOfPlayers >= 7;
        numOfKights = numOfKnightPerQuestTable[RoundNumber-1][numOfPlayers-5];
        votingStack = new Stack<>();
        RoundResult = MissionCard.WaitingForMoreKights;
    }

    public String getRoundStatus()
    {
        String result = "Round "+RoundNumber;
        String extraFailString = "\nThis Round need 2 Fail cards to Fail the mission";
        if (isSpecialRound)
            result += extraFailString;
        if (ShouldGetNewSuggetion()){
            result += "\n"+members.get(KingId).name + " should choose " + numOfKights + " Kights for the mission";
            if(votingStack.size()>0)
                result +="\nthis is the #" +(votingStack.size()) + "time for trying to set Knights for the mission";
        }
        if (votingStack != null){
            result+=ShowRoundsHistory();
        }
        if (getVoteResults() == missionVote.Approve && getRoundResult() == MissionCard.WaitingForMoreKights){
            result += "\nWaiting for the following to come back from the mission";
            result += mission.getKnightDidntComeBack();
        }
        else if (getRoundResult()!= MissionCard.WaitingForMoreKights){
            result += "\n"+ mission.GetResults();
        }
        return result;
    }

    public boolean setNewVoting(List<Integer> SuggestionList, int id){
        if (id != KingId )
            return false;
        if (SuggestionList.size() != numOfKights)
            return false;
        if(getVoteResults()==missionVote.NoOpenVoting || getVoteResults()==missionVote.Reject) {

            Voting newVote = new Voting(members, SuggestionList, numOfPlayers, KingId);
            votingStack.add(newVote);
            return true;
        }
        return false;
    }

    public boolean Vote(int id, missionVote vote){
        if(votingStack==null || votingStack.empty())
            return false;
        if (!votingStack.peek().setVote(id,vote))
            return false;
        if (votingStack.peek().getVotingResults() == missionVote.Reject && votingStack.size() ==5 ) {
            RoundResult = MissionCard.Fail;
            return true;
        }
        if (votingStack.peek().getVotingResults() == missionVote.Reject && KingId == votingStack.peek().KingId)
        {
            KingId = (KingId+1) % numOfPlayers;
        }
        if (votingStack.peek().getVotingResults() == missionVote.Approve)
        {
            mission = new Mission(members, votingStack.peek().KnightForMission,numOfKights,isSpecialRound);
        }
        return true;
    }

    boolean ShouldGetNewSuggetion(){
        if (votingStack == null || votingStack.empty() || votingStack.size() == 0 || (votingStack.peek().getVotingResults()==missionVote.Reject && votingStack.size()<5))
            return true;
        return  false;
    }
    public missionVote getVoteResults()
    {
        if (votingStack == null || votingStack.empty() || votingStack.size() == 0)
            return missionVote.NoOpenVoting;
        return votingStack.peek().getVotingResults();
    }

    String ShowRoundsHistory(){
        String result = "";
        List<Voting> list = new ArrayList<>(votingStack);
        for (Voting v : list){
            result +='\n' + v.VotingStatus();
        }
        return result;
    }
    public boolean goToMission (int id, MissionCard missionCard)
    {
        if(votingStack.empty() || votingStack.peek().voteResults != missionVote.Approve)
            return false;
        if(!mission.setMission(id,missionCard))
            return false;
        RoundResult = mission.getMissionResults();
        return true;
    }

    public MissionCard getRoundResult(){
        return RoundResult;
    }

    public int getNumOfKnights()
    {
        return numOfKights;
    }

    public String getFinalRoundResults(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nRound "+RoundNumber + " Results:");
        sb.append("\n" +mission.getFinalResults());
        return  sb.toString();
    }
}

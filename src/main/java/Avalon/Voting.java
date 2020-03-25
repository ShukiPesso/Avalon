package Avalon;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Voting {

    HashMap<Integer, member> members;
    HashMap<String, missionVote> votesTable;
    List<Integer> KnightForMission;
    int numOfPlayers;
    missionVote voteResults;
    int KingId;

    public Voting (HashMap<Integer, member> _members,List<Integer> _KinghtsForMission, int _numOfPlayers, int _KingId){
        numOfPlayers = _numOfPlayers;
        members = _members;
        KnightForMission = _KinghtsForMission;
        KingId = _KingId;
        votesTable = new HashMap<>();
    }

    public String ShowSuggestion(){
        StringBuilder sb = new StringBuilder();
        member m = members.get(KingId);
        sb.append("The Leader " + m.name +" Suggestion:");
        for (Integer id : KnightForMission){
            sb.append("\n" +members.get(id).name);
        }
        sb.append("\n\n");
        return sb.toString();
    }

    public boolean setVote (int id, missionVote vote)
    {
        member m = members.get(id);
        if(votesTable.containsKey(m.name))
            return false;
        votesTable.put(m.name,vote);
        voteResults = getVotingResults();
        return true;
    }

    public missionVote getVotingResults()
    {
        int majority = numOfPlayers/2;
        AtomicInteger numOfApprove = new AtomicInteger();
        AtomicInteger numOfReject = new AtomicInteger();
        if (votesTable==null)
            return missionVote.WaitingForMoreVoting;
        votesTable.entrySet().forEach(entry->{
            if(entry.getValue()==missionVote.Approve){
                numOfApprove.getAndIncrement();
            } else {
                numOfReject.getAndIncrement();
            }
        });
        if (numOfApprove.get() > majority)
            return missionVote.Approve;
        else if (numOfReject.get() >majority || ((numOfApprove.get()+numOfReject.get())==numOfPlayers))
            return missionVote.Reject;
        else
            return missionVote.WaitingForMoreVoting;
    }

    public String VotingStatus ()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ShowSuggestion());
        if (voteResults != null) {
            if (voteResults == missionVote.WaitingForMoreVoting)
                sb.append("Result: not enough votes for now");
            else
                sb.append("Result: " + voteResults.name());
        }
        sb.append("\nPlayers Votes:");
        votesTable.entrySet().forEach(entry-> sb.append("\n"+entry.getKey() + "\t\t\t\t" + entry.getValue()));
        if (votesTable.size()!=members.size()){
            sb.append("\nPlayers didn't vote yet");
            for (member m : members.values()){
                if (!votesTable.containsKey(m.name)){
                    sb.append("\n"+m.name);
                }
            }
            sb.append("\n\n");

        }

        return sb.toString();
    }

}

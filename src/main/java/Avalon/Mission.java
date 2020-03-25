package Avalon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Mission {
    int numOfKights;
    boolean isSpecialMission;
    HashMap<Integer, member> members;
    HashMap<String, MissionCard> MissionTable;
    Set<Integer> allowedKights;
    int numOfSuccessCards;
    int numOfFailCards;

    public Mission (HashMap<Integer, member> _members, List<Integer> _allowedKights, int _numOfKights, boolean _isSpecialMisiion){
        members = _members;
        numOfKights = _numOfKights;
        isSpecialMission = _isSpecialMisiion;
        allowedKights = new HashSet<>();
        allowedKights.addAll(_allowedKights);
    }
    public boolean setMission (int id, MissionCard mission)
    {
        if (MissionTable==null)
            MissionTable = new HashMap<>();
        if (!allowedKights.contains(id))
            return false;
        member m = members.get(id);
        if (MissionTable.containsKey(m.name))
            return false;
        MissionTable.put(m.name,mission);
        return true;
    }

    public MissionCard getMissionResults()
    {
        AtomicInteger numOfSuccess = new AtomicInteger();
        AtomicInteger numOfFail = new AtomicInteger();
        if (MissionTable==null)
            return MissionCard.WaitingForMoreKights;
        MissionTable.entrySet().forEach(entry->{
            if(entry.getValue()==MissionCard.Success){
                numOfSuccess.getAndIncrement();
            } else {
                numOfFail.getAndIncrement();
            }
        });
        if (numOfFail.get()+numOfSuccess.get() != numOfKights)
            return MissionCard.WaitingForMoreKights;
        numOfFailCards = numOfFail.get();
        numOfSuccessCards = numOfSuccess.get();
        if (numOfFail.get()>=2)
            return MissionCard.Fail;
        if (numOfFail.get() ==1 && isSpecialMission || numOfFail.get()==0)
            return MissionCard.Success;
        return MissionCard.Fail;
    }

    public String GetResults(){
        MissionCard result = getMissionResults();
        if (result == MissionCard.WaitingForMoreKights)
            return "There are no results yet";
        String KnightIMission = "\nKnights that went to mission: ";
        for (Integer id: allowedKights){
            KnightIMission+=members.get(id).name + " , ";
        }
        return "Total Result is " + result.name() +" , "+ numOfSuccessCards+ " Knights choose to Success the mission and " + numOfFailCards + " Knights choose to Fail the mission" + KnightIMission+ "\n\n";
    }

    public String getFinalResults (){
        StringBuilder sb = new StringBuilder();
        sb.append("\nPlayers Cards:");
        MissionTable.entrySet().forEach(entry-> sb.append("\n"+entry.getKey() + "\t\t\t\t" + entry.getValue()));
        return "\nMission " + getMissionResults().name() + " " + sb.toString();
    }

    public String getKnightDidntComeBack(){
        String result = "";
        if (MissionTable == null )
            MissionTable = new HashMap<>();
        for (Integer knightId : allowedKights){
            member m = members.get(knightId);
            if (!MissionTable.containsKey(m.name))
                result+="\n"+m.name;
        }
        return result;
    }
}

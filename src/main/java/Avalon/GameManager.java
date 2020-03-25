package Avalon;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameManager {
    static HashMap<Integer,Game> Games = new HashMap<>();
    static HashMap<Integer,Integer> idsMap = new HashMap<>();
    static int nextID=0;
    static Random random = new Random(LocalTime.now().toNanoOfDay());


    static int newGame(int numOfPlayers){
        Game g = new Game(numOfPlayers);
        int currentId = nextID;
        nextID++;
        Games.put(currentId,g);
        return  currentId;
    }

    static String register(int gameId, String name){
        Game g = Games.getOrDefault(gameId,null);
        if (g==null)
            return "GameID " + gameId + "does not exists";
        if (name =="")
            return "We not welcome anonymouss player ;), Please set your name and register again";
        int memberId = g.setNewMember(name);
        int mapMemberId;
        do{
            mapMemberId= random.nextInt(9970) + 10;
        } while (idsMap.containsKey(mapMemberId));
        idsMap.put(mapMemberId,memberId);
        member m = g.getMember(memberId);
        String whoamiAPI = String.format("\nFor More instruction Go To\nhttp://%s:%s/Avalon/whoami?GameId=%s&PlayerId=%s", ConfigAvalon.ip, ConfigAvalon.port,gameId,mapMemberId);
        return m.name + " Welcome to Avalon, Your Id is:" + mapMemberId + " Please use it for do your tasks in the Game \nYour Card is:" + m.charecter.name() + whoamiAPI;
    }

    static String getStatus(int gameId){
        Game g = Games.getOrDefault(gameId,null);
        if (g==null)
            return "GameID " + gameId + "does not exists";
        return g.getStatus();
    }

    static int getPlayerInnerId(int id)
    {
        if(idsMap.containsKey(id))
            return idsMap.get(id);
        return -1;
    }
    static List<Integer> parseListIds(String ListIdsStr){
        List<Integer> result = new ArrayList<>();
        String delimiter = ",";
        String[] idsStrArr = ListIdsStr.split(delimiter);
        for (String str : idsStrArr){
            int id = Integer.parseInt(str);
            result.add(id);
        }
        return result;
    }
    static String Suggest(int gameId, int playerId, String suggestIds){
        Game g = Games.getOrDefault(gameId,null);
        if (g==null)
            return "GameID " + gameId + "does not exists";
        int innerId = getPlayerInnerId(playerId);
        if(innerId ==-1)
            return "Player ID " + playerId + "does not exists";
        List<Integer> idsList = parseListIds(suggestIds);
        String result = "";
        if(g.RoundStack.peek().setNewVoting(idsList,innerId))
            result+= "Suggestion Added All should vote";
        else
            result+= "There was some error please try again.";
        return  result + "\n\n" + getStatus(gameId);
    }

    static String Vote (int gameId, int playerId, String vote){
        Game g = Games.getOrDefault(gameId,null);
        if (g==null)
            return "GameID " + gameId + "does not exists";
        int innerId = getPlayerInnerId(playerId);
        if(innerId ==-1)
            return "Player ID " + playerId + "does not exists";
        String result = "";
        if (missionVote.Approve.name().equalsIgnoreCase(vote)){
            if(g.vote(innerId,missionVote.Approve))
                result+= "You Successfully Approved the Suggestion to mission";
            else
                result+= "something went wrong, please try again \n(note you should set your Player ID you got in the registration)";
        }
        else if (missionVote.Reject.name().equalsIgnoreCase(vote)){
            if(g.vote(innerId,missionVote.Reject))
                result+= "You Successfully Reject the Suggestion to mission";
            else
                result+= "something went wrong, please try again \n(note you should set your Player ID you got in the registration)";
        }
        else
            result+= "Something went wrong, Please choose Approve Or Reject the Suggestion";
        return  result + "\n\n" + getStatus(gameId);
    }

    static String goMission (int gameId, int playerId, String missionCard){
        Game g = Games.getOrDefault(gameId,null);
        if (g==null)
            return "GameID " + gameId + "does not exists";
        int innerId = getPlayerInnerId(playerId);
        if(innerId ==-1)
            return "Player ID " + playerId + "does not exists";
        String result = "";
        if (MissionCard.Success.name().equalsIgnoreCase(missionCard)){
            if(g.mission(innerId,MissionCard.Success))
                result+= "You Successfully Set your Success Card for the mission";
            else
                result+= "something went wrong, please try again \n(note you should set your Player ID you got in the registration)";
        }
        else if (MissionCard.Fail.name().equalsIgnoreCase(missionCard)){
            if(g.mission(innerId,MissionCard.Fail))
                result+= "You Successfully Set your Fail Card for the mission";
            else
                result+= "something went wrong, please try again \n(note you should set your Player ID you got in the registration)";
        }
        else
            result+= "Something went wrong, Please choose Success Or Fail the Mission";

        return  result + "\n\n" + getStatus(gameId);
    }

    static String whoami (int gameId, int playerId){
        Game g = Games.getOrDefault(gameId,null);
        if (g==null)
            return "GameID " + gameId + "does not exists";
        int innerId = getPlayerInnerId(playerId);
        if(innerId ==-1)
            return "Player ID " + playerId + "does not exists";
        return g.whoami(innerId) + "\n" + GetAllAPIsForId(gameId,playerId);
    }

    static String Assassin(int gameId, int playerId, int MerlinId){
        Game g = Games.getOrDefault(gameId,null);
        if (g==null)
            return "GameID " + gameId + "does not exists";
        int innerId = getPlayerInnerId(playerId);
        if(innerId ==-1)
            return "Player ID " + playerId + "does not exists";
        return g.Assassin(innerId,MerlinId) + "\n\n" + getStatus(gameId);
    }

    static String GetAllAPIsForId(int GameId, int id){
        StringBuilder sb = new StringBuilder();
        sb.append("\nAPIs for the game that include you game id and player id:");
        sb.append(String.format("\nhttp://%s:%s/Avalon/Status?GameId=%s", ConfigAvalon.ip, ConfigAvalon.port,GameId));
        sb.append(String.format("\nhttp://%s:%s/Avalon/Suggest?GameId=%s&PlayerId=%s&SuggestIds=[list of inner ids (0-9) comma separated]", ConfigAvalon.ip, ConfigAvalon.port,GameId,id));
        sb.append(String.format("\nhttp://%s:%s/Avalon/Vote?GameId=%s&PlayerId=%s&VoteCard=[approve\\reject]", ConfigAvalon.ip, ConfigAvalon.port,GameId,id));
        sb.append(String.format("\nhttp://%s:%s/Avalon/Mission?GameId=%s&PlayerId=%s&MissionCard=[success\\fail]", ConfigAvalon.ip, ConfigAvalon.port,GameId,id));
        sb.append(String.format("\nhttp://%s:%s/Avalon/Assassin?GameId=%s&PlayerId=%s&MerlinId=[]merlin inner id (0-9)", ConfigAvalon.ip, ConfigAvalon.port,GameId,id));
        return sb.toString();
    }
}

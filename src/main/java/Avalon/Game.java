package Avalon;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.*;

public class Game {
    int gameId;
    int numOfPlayers;
    int numOfKights;
    int numOfMiniuns;
    int id;
    int kingId;
    int round;
    int knightScore;
    int minionOfMordredScore;
    Boolean AssasinMerlin = null;


    HashMap<Integer, member> members;
    TreeMap<Integer, charecters> cards;
    Random random;
    Stack<Round> RoundStack;

    public Game(int _numOfPalyers)
    {
        numOfPlayers=_numOfPalyers;
        setNumOfKights();
        members = new HashMap<>();
        id =0;
        startGame();
    }

    public void restartGame(){
        startGame();
        reshuffleCards();
        nextRound();
    }
    public void startGame()
    {
        setCards();
        round=1;
        minionOfMordredScore=0;
        knightScore=0;
        kingId = -1;
        RoundStack = new Stack<>();
    }

    public void reshuffleCards(){
        for (Map.Entry<Integer, member> entry : members.entrySet()){
            charecters myChar = cards.firstEntry().getValue();
            cards.remove(cards.firstKey());
            entry.getValue().setCharacter(myChar);
        }
    }

    public void nextRound(){
        if (RoundStack != null && !RoundStack.empty())
        {
            round++;
        }
        kingId = getNextKing();
        Round r =new Round(members,round,numOfPlayers,kingId);
        RoundStack.push(r);
    }

    public boolean suggestsKightsForMission (int knightId, List<Integer> suggestion){
        return RoundStack.peek().setNewVoting(suggestion,knightId);
    }

    public boolean isRoundFinished(){
        if (RoundStack.peek().getRoundResult() != MissionCard.WaitingForMoreKights) { // Round finished
            if(RoundStack.peek().getRoundResult()==MissionCard.Success)
                knightScore++;
            else
                minionOfMordredScore++;
            return true;
        }
        return false;
    }

    public boolean isGameFinish(){
        return (minionOfMordredScore ==3 || knightScore ==3);
    }

    public boolean vote (int KightId, missionVote vote){
        if (RoundStack.peek().Vote(KightId,vote)){
            if(isRoundFinished() && !isGameFinish()){
                kingId = RoundStack.peek().KingId;
                nextRound();
            }
            return true;
        }
        return false;
    }

    public boolean mission (int id, MissionCard card){
        if (RoundStack.peek().goToMission(id,card)){
            if(isRoundFinished() && !isGameFinish()){
                kingId = RoundStack.peek().KingId;
                nextRound();
            }
            return true;
        }
        return false;
    }
    public int setNewMember(String name)
    {
        if (members.size() == numOfPlayers || name=="")
            return -1;
        int myId = id;
        id++;
        charecters myChar = cards.firstEntry().getValue();
        cards.remove(cards.firstKey());
        member m = new member(name,myId,myChar);
        members.put(myId,m);
        // in case all Palyers registered start the first round
        if (members.size() == numOfPlayers){
            nextRound();
        }
        return myId;
    }

    public boolean setNumOfKights ()
    {
        switch (numOfPlayers)
        {
            case 5:
                numOfKights=1;
                numOfMiniuns =0;
                break;
            case 6:
                numOfKights=2;
                numOfMiniuns=0;
                break;
            case 7:
                numOfKights=2;
                numOfMiniuns=1;
                break;
            case 8:
                numOfKights=3;
                numOfMiniuns=1;
                break;
            case 9:
                numOfKights=4;
                numOfMiniuns=1;
                break;
            case 10:
                numOfKights=4;
                numOfMiniuns=2;
                break;
            default:
                return false;
        }
        return true;
    }

   public void setCards ()
   {
       random = new Random(LocalTime.now().getNano());
       cards = new TreeMap<Integer, charecters>();
       cards.put(random.nextInt(),charecters.Merlin);
       cards.put(random.nextInt(),charecters.Assassian);
       cards.put(random.nextInt(),charecters.Mordered);
       cards.put(random.nextInt(),charecters.Percival);
       for (int i = 0; i< numOfMiniuns ; i++)
       {
           cards.put(random.nextInt(),charecters.Miniun_of_morderd);
       }
       for (int i = 0; i< numOfKights ; i++)
       {
           cards.put(random.nextInt(),charecters.Knight);
       }
   }

   public member getMember (int id)
   {
       return members.get(id);
   }

   public int getNextKing()
   {
       if (kingId == -1)
       {
           kingId = getFirstKing();
       }
       else
       {
           kingId = (kingId+1)%numOfPlayers;
       }
       return kingId;
   }

   public int getFirstKing()
   {
       Random generator = new Random();
       Object[] values = members.values().toArray();
       Object randomValue = values[generator.nextInt(values.length)];
       return ((member)randomValue).id;
   }

   String ShowRoundsHistory(){
        String result = "";
        List<Round> list = new ArrayList<>(RoundStack);
        for (Round r : list){
            result +='\n' + r.getRoundStatus();
        }
        return result;
   }
   public String getStatus()
   {
       String result = "";
       if (isGameFinish()){
           if (knightScore ==3 ) {
               if (AssasinMerlin==null) {
                   result += "Knights Finish Successfully 3 Missions";
                   result += "Assassin now have a chance to Assassinate in Merlin";
                   result += getMinionOfMordred();
                   result += ShowRoundsHistory();
               } else if (AssasinMerlin == false) {
                   result+= "Knight Won!!!";
                   result+= getNameOfCharecter(charecters.Assassian) +" Who was Assassin failed to Assassinate in Merlin (who was:"+getNameOfCharecter(charecters.Merlin)+")\n";
                   result += GetFinalResults();
               }
               else if (AssasinMerlin == true){
                   result += "Minion Of Mordred Won!!!";
                   result += "Knights Finish Successfully 3 Missions";
                   result+= getNameOfCharecter(charecters.Assassian) +" Who was Assassin Success to Assassinate in Merlin:"+getNameOfCharecter(charecters.Merlin)+"\n";
                   result += GetFinalResults();
               }
           }
           else {
               result += "Minion Of Mordred Won!!!";
               result += GetFinalResults();
           }

       }
       else{
           String userInput = "Avalon Game id {0} Round {1} Knight score {2} Minion of Mordred Score {3}";
           result = MessageFormat.format(userInput, gameId, round, knightScore,minionOfMordredScore);
           if (RoundStack != null)
               result+= ShowRoundsHistory();
       }

       StringBuilder sb = new StringBuilder();
       sb.append("\nPlayers Ids:");
       members.entrySet().forEach(entry-> sb.append("\n"+entry.getValue().name + "\t\t\t\t"+ entry.getKey()));

       return result + sb.toString() ;
   }

    private String GetFinalResults() {
        String result ="\nRounds Results";
        List<Round> roundList = new ArrayList<>(RoundStack);
        for (Round round : roundList){
            result += "\n"+round.getFinalRoundResults();
        }
        return result;
    }

    public String Assassin(int AssassinId, int MerlinId){
        if (!isGameFinish() || (isGameFinish()&& knightScore <3) || AssasinMerlin != null)
            return "it's just not the time to try assassinate!, try another time...";
        if (members.get(AssassinId).charecter != charecters.Assassian)
            return "You Are not assassin and have no permission to try to assassinate!";
        if (members.get(MerlinId).charecter == charecters.Merlin){
            AssasinMerlin = true;
            return "You Successfully assassinate Merlin!";
        }
        else {
            AssasinMerlin = false;
            return "you failed to assassinate Merlin!";
        }
    }
    private String getMinionOfMordred() {
        StringBuilder sb = new StringBuilder();
        sb.append("Minion Of Mordred are:");
        for (member m : members.values()){
            if (isMinionOfMordred(m.charecter)){
                sb.append("\n"+m.name+"\t\t\t\t"+m.charecter.name());
            }
        }
        sb.append("\n");
        return  sb.toString();
    }

    public String whoami(int playerID){
        member m = members.getOrDefault(playerID,null);
        if (m==null)
            return "Player does not exist";
        StringBuilder sb = new StringBuilder();
        sb.append("Hello " + m.name +" you are " + m.charecter.name());
        List<charecters> charecterList = new ArrayList<>();
        charecterList.add(Avalon.charecters.Miniun_of_morderd);
        charecterList.add(Avalon.charecters.Assassian);
        if(needToSeeOthers(m.charecter)){
            sb.append("\nThe Players You Should Know are:\n");
            if (m.charecter == charecters.Percival)
                sb.append(getNameOfCharecter(charecters.Merlin));
            else if (m.charecter == charecters.Merlin)
                sb.append(getNameOfCharectersList(charecterList));
            else if (isMinionOfMordred(m.charecter)){
                charecterList.add(charecters.Mordered);
                sb.append(getNameOfCharectersList(charecterList));
            }
        }
        return sb.toString();
   }

   String getNameOfCharectersList (List<charecters> ca){
        StringBuilder sb = new StringBuilder();
        for (charecters c : ca){
            sb.append(getNameOfCharecter(c));
        }
        return sb.toString();
   }
   String getNameOfCharecter(charecters c){
        String result = "";
        for (member m : members.values()){
            if (m.charecter ==c)
                result +=m.name+"\n";
        }
        return  result;
   }

   boolean needToSeeOthers(charecters c){
        return c==charecters.Merlin || isMinionOfMordred(c) || c==charecters.Percival;
   }
   boolean isMinionOfMordred(charecters c){
        return merlinShouldSeeHim(c) || c==charecters.Mordered;
   }

   boolean merlinShouldSeeHim(charecters c){
       return c==charecters.Miniun_of_morderd || c== charecters.Assassian;
   }
}

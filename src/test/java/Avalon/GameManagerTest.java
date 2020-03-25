package Avalon;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameManagerTest {

    @Test
    public void newGame() {
        int numOfPalyers = GameManager.random.nextInt(6)+5;
        int GamedId = GameManager.newGame(numOfPalyers);

        for (int i=0; i< numOfPalyers ; i++)
        {
            String name = RandomString.getAlphaNumericString(10);
            String str = GameManager.register(GamedId,name);
        }
        assertEquals(numOfPalyers,GameManager.idsMap.size());
        List<Integer> memberList = new ArrayList<>(GameManager.idsMap.keySet());
        GameManager.getStatus(GamedId);
        int KingId = GameManager.Games.get(GamedId).kingId;

    }

    @Test
    public void register() {
    }
}